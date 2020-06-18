package com.shyun.kr.domain.common;

public enum ResultCode {
  SUCCESS,
  FAIL,
  BAD_REQUEST,

  COUPON_NOT_FOUND,
  USER_NOT_FOUND,
  COUPON_ALREADY_USE,
  COUPON_EXPIRED,

  LOGIN_FAIL,
  DUPLICATED_USER;
}
