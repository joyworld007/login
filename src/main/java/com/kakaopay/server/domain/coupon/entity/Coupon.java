package com.kakaopay.server.domain.coupon.entity;

import com.kakaopay.server.domain.coupon.CouponStatus;
import com.kakaopay.server.domain.coupon.dto.CouponDto;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "coupon")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Coupon {

  // 쿠폰 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 쿠폰 상태
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  @Setter
  private CouponStatus status;

  // 쿠폰 만료일
  @Column(name = "expire_date")
  private LocalDateTime expireDate;

  // 쿠폰 생성일
  @Column(name = "create_date")
  private LocalDateTime createDate;

  @Embedded
  private CouponIssue couponIssue;

  public void setCouponIssue(CouponIssue couponIssue) {
    if (Optional.ofNullable(couponIssue).isPresent()) {
      this.couponIssue = couponIssue;
      this.status = CouponStatus.ISSUED;
    }
  }

  public void Coupon(CouponStatus status, LocalDateTime expireDate, LocalDateTime createDate) {
    this.status = status;
    this.expireDate = expireDate;
    this.createDate = createDate;
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(this.expireDate);
  }

  public void couponCancel() {
    this.status = CouponStatus.ISSUED;
    this.couponIssue = null;
  }

  protected Coupon(CouponDto dto) {
    this.id = dto.getId();
    this.status = dto.getStatus();
    this.expireDate = dto.getExpireDate();
    this.createDate = dto.getCreateDate();

    CouponIssue couponIssue = new CouponIssue(dto.getId()
        , dto.getUserId()
        , dto.getUseDate()
        , dto.getIssueDate()
    );
    this.setCouponIssue(couponIssue);
  }

  public static Coupon ofDto(CouponDto dto) {
    return new Coupon(dto);
  }

}
