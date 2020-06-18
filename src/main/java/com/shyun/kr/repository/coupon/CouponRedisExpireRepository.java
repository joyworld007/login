package com.shyun.kr.repository.coupon;

import com.shyun.kr.domain.coupon.dto.CouponExpire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRedisExpireRepository extends CrudRepository<CouponExpire, String> {

}
