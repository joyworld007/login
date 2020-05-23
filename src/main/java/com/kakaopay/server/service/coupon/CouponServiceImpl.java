package com.kakaopay.server.service.coupon;

import com.kakaopay.server.domain.coupon.CouponStatus;
import com.kakaopay.server.domain.coupon.dao.CouponDao;
import com.kakaopay.server.domain.coupon.entity.Coupon;
import com.kakaopay.server.repository.coupon.CouponJdbcRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService {

  @Autowired
  CouponJdbcRepository couponJdbcRepository;

  final static int BATCH_SIZE = 10000;

  @Override
  public void creat(Long size) {

    LocalDateTime expireDate = LocalDateTime.now();
    List<CouponDao> couponDaoList = new ArrayList<>();
    for (long i = 1; i <= size; i++) {
      couponDaoList.add(
          CouponDao.builder()
              .expireDate(expireDate)
              .status(CouponStatus.CREATED.toString()).build()
      );
      if (i % BATCH_SIZE == 0) {
        couponJdbcRepository.createCoupon(couponDaoList);
        couponDaoList.clear();
      }
    }
    couponJdbcRepository.createCoupon(couponDaoList);
  }

  @Override
  public Coupon createCouponIssue(String userId) {
    return null;
  }

  @Override
  public List<Coupon> findCouponIssueByUserId(String UserId) {
    return null;
  }

  @Override
  public void useCoupon(Long CouponId) {

  }

  @Override
  public void cancelCoupon(Long CouponId) {

  }

  @Override
  public List<Coupon> selectTodayExpiredCoupon() {
    return null;
  }


}
