package com.kakaopay.server.domain.coupon.dao;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CouponDao {

  private String status;
  private LocalDateTime expireDate;

}
