package com.exam.coupon.domain.coupon.converter;

import com.exam.coupon.domain.common.BaseConverter;
import com.exam.coupon.domain.coupon.dto.CouponDto;
import com.exam.coupon.domain.coupon.entity.Coupon;

public class CouponConverter extends BaseConverter<CouponDto, Coupon> {

  public CouponConverter() {
    super(Coupon::ofDto, CouponDto::ofEntity);
  }

}
