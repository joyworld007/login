package com.kakaopay.server.domain.coupon;

import com.kakaopay.server.domain.coupon.entity.Coupon;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class CouponSpec {

//  public static Specification<Coupon> searchCoupon() {
//    return new Specification<Coupon>() {
//      @Override
//      public Predicate toPredicate(Root<Coupon> root,
//          CriteriaQuery<?> criteriaQuery,
//          CriteriaBuilder criteriaBuilder) {
//        return criteriaBuilder.greaterThan(root.get(Coupon_.id), 10);
//
//      }
//    };
//  }
}
