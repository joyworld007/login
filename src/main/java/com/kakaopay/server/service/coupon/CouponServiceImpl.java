package com.kakaopay.server.service.coupon;

import com.kakaopay.server.domain.common.Result;
import com.kakaopay.server.domain.coupon.CouponStatus;
import com.kakaopay.server.domain.coupon.ResultCode;
import com.kakaopay.server.domain.coupon.converter.CouponConverter;
import com.kakaopay.server.domain.coupon.dto.CouponDto;
import com.kakaopay.server.domain.coupon.entity.Coupon;
import com.kakaopay.server.domain.coupon.entity.CouponIssue;
import com.kakaopay.server.repository.coupon.CouponJdbcRepository;
import com.kakaopay.server.repository.coupon.CouponJpaRepository;
import com.kakaopay.server.repository.coupon.CouponRedisRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CouponServiceImpl implements CouponService {

  private final CouponJdbcRepository couponJdbcRepository;

  private final CouponJpaRepository couponJpaRepository;

  private final CouponRedisRepository couponRedisRepository;

  final static int BATCH_SIZE = 10000;

  @Override
  @Transactional
  public ResultCode generate(Long size) {

    LocalDateTime expireDate = LocalDateTime.now();
    List<CouponDto> couponDtoList = new ArrayList<>();
    try {
      for (long i = 1; i <= size; i++) {
        couponDtoList.add(
            CouponDto.builder()
                .expireDate(expireDate)
                .status(CouponStatus.CREATED).build()
        );
        if (i % BATCH_SIZE == 0) {
          couponJdbcRepository.createCoupon(couponDtoList);
          expireDate = LocalDateTime.now().plusDays(1);
          couponDtoList.clear();
        }
      }
      couponJdbcRepository.createCoupon(couponDtoList);
    } catch (Exception e) {
      return ResultCode.FAIL;
    }
    return ResultCode.SUCCESS;
  }

  @Override
  @Transactional
  public ResultCode create(CouponDto couponDto) {
    try {
      Coupon coupon = Coupon.ofDto(couponDto);
      coupon.setStatus(CouponStatus.CREATED);
      coupon.setCreateDate(LocalDateTime.now());
      couponJpaRepository.save(coupon);
    } catch (Exception e) {
      return ResultCode.FAIL;
    }
    return ResultCode.SUCCESS;
  }

  @Override
  @Transactional
  public ResultCode updateCoupon(Long id, CouponDto couponDto) {
    Optional<Coupon> coupon = couponJpaRepository.findById(id);
    if (coupon.isPresent()) {
      //쿠폰 사용기간 만료 체크
      if (coupon.get().isExpired()) {
        return ResultCode.COUPON_EXPIRED;
      }
      if (CouponStatus.ISSUED.equals(couponDto.getStatus())
          && CouponStatus.CREATED.equals(coupon.get().getStatus())
          && Optional.ofNullable(couponDto.getUserId()).isPresent()) {
        //사용자 에게 쿠폰 지급
        //쿠폰 발급일 업데이트
        couponDto.setIssueDate(LocalDateTime.now());
        //쿠폰을 발급 처리
        coupon.get().setCouponIssue(CouponIssue.ofDto(couponDto));
      } else if (CouponStatus.USED.equals(couponDto.getStatus())
          && CouponStatus.ISSUED.equals(coupon.get().getStatus())) {
        //쿠폰 사용 처리
        couponDto.setIssueDate(coupon.get().getCouponIssue().getIssueDate());
        couponDto.setUseDate(LocalDateTime.now());
        couponDto.setUserId(coupon.get().getCouponIssue().getUserId());
        coupon.get().setCouponUse(CouponIssue.ofDto(couponDto));
      } else if (CouponStatus.ISSUED.equals(couponDto.getStatus())
          && CouponStatus.USED.equals(coupon.get().getStatus())) {
        //쿠폰 사용 취소
        couponDto.setIssueDate(coupon.get().getCouponIssue().getIssueDate());
        couponDto.setUserId(coupon.get().getCouponIssue().getUserId());
        couponDto.setUseDate(null);
        //쿠폰을 사용 취소 처리
        coupon.get().setCouponUseCancel(CouponIssue.ofDto(couponDto));
      } else {
        return ResultCode.BAD_REQUEST;
      }
      couponJpaRepository.save(coupon.get());
      couponRedisRepository.save(CouponDto.ofEntity(coupon.get()));
    } else {
      return ResultCode.COUPON_NOT_FOUND;
    }
    return ResultCode.SUCCESS;
  }

  @Override
  public Result findCouponByUserId(String UserId, Pageable pageable) {
    return Result.builder().entry(
        new CouponConverter()
            .covertFromEntities(
                couponJpaRepository.findAllByCouponIssue_UserId(UserId, pageable).toList()
            )
    ).build();
  }

  @Override
  public Result findTodayExpiredCoupon(Pageable pageable) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return Result.builder().entry(
        new CouponConverter()
            .covertFromEntities(
                couponJpaRepository.findByExpireDateIsBetweenAndStatus(
                    LocalDateTime.parse(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            + " 00:00:00"
                        , formatter)
                    , LocalDateTime.parse(LocalDateTime.now().plusDays(1)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00"
                        , formatter)
                    , CouponStatus.ISSUED
                    , pageable).toList()
            )
    ).build();
  }

  @Override
  public Result findById(Long id) {
    // Redis에서 쿠폰 조회
    Optional<CouponDto> couponDto = couponRedisRepository.findById(id);
    if (couponDto.isPresent()) {
      return Result.builder().entry(couponDto.get()).build();
    } else {
      // Redis에 결과가 없다면 DB에서 조회
      Optional<Coupon> coupon = couponJpaRepository.findById(id);
      if (coupon.isPresent()) {
        CouponDto couponDto1 = new CouponConverter().convertFromEntity(coupon.get());
        // Redis에 저장
        couponRedisRepository.save(couponDto1);
        return Result.builder().entry(couponDto1).build();
      } else {
        return Result.builder().entry(null).build();
      }
    }
  }

}
