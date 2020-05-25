package com.kakaopay.server.coupon;

import com.kakaopay.server.controller.CouponController;
import com.kakaopay.server.repository.coupon.CouponJpaRepository;
import com.kakaopay.server.repository.coupon.CouponRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(value = CouponController.class)
public class CouponServiceTest {

  @MockBean
  private CouponRedisRepository couponRedisRepository;

  @MockBean
  private CouponJpaRepository couponJpaRepository;

  @Test
  public void createCouponTest() {

  }


}
