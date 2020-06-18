package com.exam.coupon.domain.common;

public class JwtProperties {

  //secret key
  public static final String SECRET = "kakaopay";
  //토큰 만료일 10일
  public static final int EXPIRATION_TIME = 864000000;
  //JWT Token의 prefix는 Bearer
  public static final String TOKEN_PREFIX = "Bearer ";
  //JWT Token은 Authorization header로 전달
  public static final String HEADER_STRING = "Authorization";
}
