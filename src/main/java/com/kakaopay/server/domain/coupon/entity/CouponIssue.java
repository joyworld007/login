package com.kakaopay.server.domain.coupon.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class CouponIssue {

  @Transient
  private String couponId;

  // 사용자 아이디
  @Column(name = "user_id")
  private String userId;

  // 쿠폰 사용일
  @Column(name = "use_date")
  private LocalDateTime useDate;

  // 쿠폰 발급일
  @Column(name = "issue_date")
  private LocalDateTime issueDate;

  protected CouponIssue() {
  }

  public void CouponIssue(String couponId, String userId, LocalDateTime useDate,
      LocalDateTime issueDate) {
    this.couponId = couponId;
    this.userId = userId;
    this.useDate = useDate;
    this.issueDate = issueDate;
  }

}
