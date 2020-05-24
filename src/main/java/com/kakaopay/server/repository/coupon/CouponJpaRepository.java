package com.kakaopay.server.repository.coupon;

import com.kakaopay.server.domain.coupon.entity.Coupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

  List<Coupon> findAllByCouponIssue_UserId(String userId);
}
