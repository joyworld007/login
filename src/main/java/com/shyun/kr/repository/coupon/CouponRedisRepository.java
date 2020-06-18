package com.shyun.kr.repository.coupon;

import com.shyun.kr.domain.coupon.dto.CouponDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRedisRepository extends CrudRepository<CouponDto, Long> {

}
