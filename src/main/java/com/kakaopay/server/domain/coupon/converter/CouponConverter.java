package com.kakaopay.server.domain.coupon.converter;

import com.kakaopay.server.domain.common.BaseConverter;
import com.kakaopay.server.domain.coupon.dto.CouponDto;
import com.kakaopay.server.domain.coupon.entity.Coupon;

public class CouponConverter extends BaseConverter<CouponDto, Coupon> {

  public CouponConverter() {
    super(Coupon::ofDto, CouponDto::ofEntity);
  }

}
