package com.kakaopay.server.domain.coupon.entity;

import com.kakaopay.server.domain.coupon.dto.CouponDto;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponIssue {

  // 사용자 아이디
  @Column(name = "user_id")
  private String userId;

  // 쿠폰 사용일
  @Column(name = "use_date")
  private LocalDateTime useDate;

  // 쿠폰 발급일
  @Column(name = "issue_date")
  private LocalDateTime issueDate;

  public CouponIssue(String userId, LocalDateTime useDate,
      LocalDateTime issueDate) {
    this.userId = userId;
    this.useDate = useDate;
    this.issueDate = issueDate;
  }

  public CouponIssue(CouponDto couponDto) {
    this.userId = couponDto.getUserId();
    this.useDate = couponDto.getUseDate();
    this.issueDate = couponDto.getIssueDate();
  }

  public static CouponIssue ofDto(CouponDto couponDto) {
    CouponIssue couponIssue = new CouponIssue(
        couponDto.getUserId()
        , couponDto.getUseDate()
        , couponDto.getIssueDate()
    );
    return couponIssue;
  }

}
