package com.kakaopay.server.repository.coupon;

import com.kakaopay.server.domain.coupon.CouponStatus;
import com.kakaopay.server.domain.coupon.entity.Coupon;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

  Page<Coupon> findAllByCouponIssue_UserId(String userId, Pageable pageable);

  Page<Coupon> findByExpireDateIsBetweenAndStatus(LocalDateTime start, LocalDateTime end,
      CouponStatus status, Pageable pageable);
}
