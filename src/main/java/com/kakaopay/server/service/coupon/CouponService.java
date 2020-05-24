package com.kakaopay.server.service.coupon;

import com.kakaopay.server.domain.coupon.ResultCode;
import com.kakaopay.server.domain.coupon.dto.CouponDto;
import com.kakaopay.server.domain.coupon.entity.Coupon;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface CouponService {

  // size 만큼 쿠폰 생성
  @Transactional
  ResultCode creat(Long size);

  // 쿠폰을 사용자 에게 지급
  @Transactional
  ResultCode issueCoupon(Long id, CouponDto couponDto);

  // 사용자 에게 지급된 쿠폰을 조회
  List<Coupon> findCouponIssueByUserId(String UserId);

  // 쿠폰 사용
  @Transactional
  ResultCode useCoupon(Long CouponId);

  // 쿠폰 사용 취소
  @Transactional
  ResultCode cancelCoupon(Long CouponId);

  //오늘 만료된 쿠폰 list 조회
  List<Coupon> selectTodayExpiredCoupon();

}
