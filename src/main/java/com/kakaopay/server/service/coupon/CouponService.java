package com.kakaopay.server.service.coupon;

import com.kakaopay.server.domain.coupon.entity.Coupon;

import javax.transaction.Transactional;
import java.util.List;

public interface CouponService {
    // size 만큼 쿠폰 생성
    void Creat(Long size);

    // 쿠폰을 사용자 에게 지급
    Coupon createCouponIssue(String userId);

    // 사용자 에게 지급된 쿠폰을 조회
    List<Coupon> findCouponIssueByUserId(String UserId);

    // 쿠폰 사용
    @Transactional
    void useCoupon(Long CouponId);

    // 쿠폰 사용 취소
    @Transactional
    void cancelCoupon(Long CouponId);

    //오늘 만료된 쿠폰 list 조회
    List<Coupon> selectTodayExpiredCoupon();

}
