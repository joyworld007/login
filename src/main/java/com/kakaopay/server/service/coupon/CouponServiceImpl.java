package com.kakaopay.server.service.coupon;

import com.kakaopay.server.domain.coupon.CouponStatus;
import com.kakaopay.server.domain.coupon.ResultCode;
import com.kakaopay.server.domain.coupon.dao.CouponDao;
import com.kakaopay.server.domain.coupon.dto.CouponDto;
import com.kakaopay.server.domain.coupon.entity.Coupon;
import com.kakaopay.server.domain.coupon.entity.CouponIssue;
import com.kakaopay.server.repository.coupon.CouponJdbcRepository;
import com.kakaopay.server.repository.coupon.CouponJpaRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

  @Autowired
  CouponJdbcRepository couponJdbcRepository;

  @Autowired
  CouponJpaRepository couponJpaRepository;

  final static int BATCH_SIZE = 10000;

  @Override
  @Transactional
  public ResultCode creat(Long size) {

    LocalDateTime expireDate = LocalDateTime.now();
    List<CouponDao> couponDaoList = new ArrayList<>();
    try {
      for (long i = 1; i <= size; i++) {
        couponDaoList.add(
            CouponDao.builder()
                .expireDate(expireDate)
                .status(CouponStatus.CREATED).build()
        );
        if (i % BATCH_SIZE == 0) {
          couponJdbcRepository.createCoupon(couponDaoList);
          expireDate = LocalDateTime.now().plusDays(1);
          couponDaoList.clear();
        }
      }
      couponJdbcRepository.createCoupon(couponDaoList);
    } catch (Exception e) {
      return ResultCode.FAIL;
    }
    return ResultCode.SUCCESS;
  }

  @Override
  @Transactional
  public ResultCode issueCoupon(Long id, CouponDto couponDto) {
    Optional<Coupon> coupon = couponJpaRepository.findById(id);
    if (coupon.isPresent()) {
      log.info("1111");
      if (coupon.get().isExpired()) {
        log.info("2222");
        return ResultCode.COUPON_EXPIRED;
      }

      System.out.println("coupon: " + coupon);

      coupon.get().setCouponIssue(CouponIssue.ofDto(couponDto));

      couponJpaRepository.save(coupon.get());
    } else {
      return ResultCode.COUPON_NOT_FOUND;
    }
    return ResultCode.SUCCESS;
  }

  @Override
  public List<Coupon> findCouponIssueByUserId(String UserId) {
    return null;
  }

  @Override
  public ResultCode useCoupon(Long CouponId) {
    return ResultCode.SUCCESS;
  }

  @Override
  public ResultCode cancelCoupon(Long CouponId) {
    return ResultCode.SUCCESS;
  }

  @Override
  public List<Coupon> selectTodayExpiredCoupon() {
    return null;
  }

}
