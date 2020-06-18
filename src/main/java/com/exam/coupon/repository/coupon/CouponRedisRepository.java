package com.exam.coupon.repository.coupon;

import com.exam.coupon.domain.coupon.dto.CouponDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRedisRepository extends CrudRepository<CouponDto, Long> {

}
