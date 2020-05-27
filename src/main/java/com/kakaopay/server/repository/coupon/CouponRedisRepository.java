package com.kakaopay.server.repository.coupon;

import com.kakaopay.server.domain.coupon.dto.CouponDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRedisRepository extends CrudRepository<CouponDto, Long> {

}
