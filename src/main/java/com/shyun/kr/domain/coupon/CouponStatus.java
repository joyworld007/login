package com.shyun.kr.domain.coupon;

/**
 * 쿠폰 상태 CREATE : 쿠폰 생성
 * ISSUE : 사용자 에게 쿠폰 발급 완료
 * USE : 쿠폰 사용 완료
 */
public enum CouponStatus {
  CREATED,
  ISSUED,
  USED
}
