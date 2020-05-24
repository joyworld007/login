package com.kakaopay.server.domain.coupon;

public enum ResultCode {
  SUCCESS,
  FAIL,
  BAD_REQUEST,
  COUPON_NOT_FOUND,
  COUPON_ISSUE_NOT_FOUND,
  USER_NOT_FOUND,
  COUPON_ALREADY_USE,
  COUPON_EXPIRED;
}
