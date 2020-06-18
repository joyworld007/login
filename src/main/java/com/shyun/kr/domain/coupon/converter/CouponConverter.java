package com.shyun.kr.domain.coupon.converter;

import com.shyun.kr.domain.common.BaseConverter;
import com.shyun.kr.domain.coupon.dto.CouponDto;
import com.shyun.kr.domain.coupon.entity.Coupon;

public class CouponConverter extends BaseConverter<CouponDto, Coupon> {

  public CouponConverter() {
    super(Coupon::ofDto, CouponDto::ofEntity);
  }

}
