package com.exam.coupon.repository.coupon;

import com.exam.coupon.domain.coupon.dto.CouponExpire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRedisExpireRepository extends CrudRepository<CouponExpire, String> {

}
