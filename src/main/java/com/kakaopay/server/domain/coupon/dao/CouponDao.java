package com.kakaopay.server.domain.coupon.dao;

import com.kakaopay.server.domain.coupon.CouponStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CouponDao {

  private Long id;
  private CouponStatus status;
  private LocalDateTime expireDate;
  private LocalDateTime createDate;
  private String userId;
  private LocalDateTime useDate;
  private LocalDateTime issueDate;
}
