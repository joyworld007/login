package com.kakaopay.server.domain.coupon.entity;

import com.kakaopay.server.domain.coupon.CouponStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Table(name="coupon")
@Entity
public class Coupon {

    // 쿠폰 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 쿠폰 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CouponStatus status;

    // 쿠폰 만료일
    @Column(name = "expired_date")
    private LocalDateTime expireDate;

    // 쿠폰 생성일
    @Column(name = "created_date")
    private LocalDateTime createDate;

    @Embedded
    private CouponIssue couponIssue;

    protected Coupon() {}

    public void setCouponIssue(CouponIssue couponIssue) {
        if(Optional.ofNullable(couponIssue).isPresent()) {
            this.couponIssue = couponIssue;
            this.status = CouponStatus.ISSUE;
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
        this.status = CouponStatus.ISSUE;
        this.couponIssue = null;
    }

}
