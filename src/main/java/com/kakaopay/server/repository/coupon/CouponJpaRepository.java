package com.kakaopay.server.repository.coupon;

import com.kakaopay.server.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

}
