package com.exam.coupon.service.coupon;

import com.exam.coupon.domain.common.Result;
import com.exam.coupon.domain.common.ResultCode;
import com.exam.coupon.domain.coupon.dto.CouponDto;
import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CouponService {

  // size 만큼 쿠폰 생성
  @Transactional
  ResultCode generate(Long size);

  @Transactional
  ResultCode generateCsv() throws IOException;

  @Transactional
  ResultCode create(CouponDto coupon);

  // status ISSUED 일때 쿠폰을 사용자 에게 지급
  // status USED 일때 쿠폰을 사용
  // status USED -> ISSUED로 쿠폰 사용 취소
  @Transactional
  ResultCode updateCoupon(Long id, CouponDto couponDto);

  // 사용자의 쿠폰 발급 리스트
  Result<List<CouponDto>> findCouponByUserId(String UserId, Pageable pageable);

  //오늘 만료된 쿠폰 list 조회
  Result<List<CouponDto>> findTodayExpiredCoupon(Pageable pageable);

  void notifyExpireCoupon(Long day);

  //쿠폰 정보를 조회
  Result<CouponDto> findById(Long id);

  //발급된 쿠폰을 만료 일자를 키로 List로 저장
  public void mergeCouponForExpireDate(CouponDto couponDto);

}
