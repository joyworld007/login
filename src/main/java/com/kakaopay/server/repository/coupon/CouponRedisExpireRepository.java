package com.kakaopay.server.repository.coupon;

import com.kakaopay.server.domain.coupon.dto.CouponExpire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRedisExpireRepository extends CrudRepository<CouponExpire, String> {

}
