package com.kakaopay.server.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.kakaopay.server.domain.common.ResultCode;
import com.kakaopay.server.domain.coupon.CouponStatus;
import com.kakaopay.server.domain.coupon.dto.CouponDto;
import com.kakaopay.server.domain.coupon.entity.Coupon;
import com.kakaopay.server.repository.coupon.CouponJdbcRepository;
import com.kakaopay.server.repository.coupon.CouponJpaRepository;
import com.kakaopay.server.repository.coupon.CouponRedisRepository;
import com.kakaopay.server.service.coupon.CouponServiceImpl;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

  @Mock
  private CouponJpaRepository couponJpaRepository;

  @Mock
  private CouponJdbcRepository couponJdbcRepository;

  @Mock
  private CouponRedisRepository couponRedisRepository;

  @InjectMocks
  private CouponServiceImpl couponService;

  private CouponDto couponDto;
  private Coupon coupon;

  @BeforeEach
  public void setup(TestInfo testInfo) {
    rawSetup(testInfo);
  }

  private void rawSetup(TestInfo testInfo) {

    Method method = testInfo.getTestMethod().orElseThrow();
    couponDto = CouponDto.builder()
        .status(CouponStatus.CREATED).build();
    coupon = Coupon.ofDto(couponDto);

  }

  @DisplayName("Coupon Generate Test")
  @Test
  public void couponGenerateTest() {
    try {
      // given
      when(couponJdbcRepository.createCoupon(anyList())).thenReturn(1);
    } catch (Exception e) {
    }
    ResultCode resultCode = couponService.generate(anyLong());
    // then
    assertThat(resultCode).isEqualTo(ResultCode.SUCCESS);
  }

  @DisplayName("Coupon create Test")
  @Test
  public void couponCreateTest() {

    // given
    when(couponJpaRepository.save(any())).thenReturn(coupon);
    when(couponRedisRepository.save(any())).thenReturn(couponDto);
    ResultCode resultCode = couponService.create(couponDto);
    // then
    assertThat(resultCode).isEqualTo(ResultCode.SUCCESS);
  }

  @DisplayName("Coupon create Exception Test")
  @Test
  public void couponCreateExceptionTest() {
    CouponDto couponDto = CouponDto.builder()
        .status(CouponStatus.CREATED).build();
    Coupon coupon = Coupon.ofDto(couponDto);
    // given
    when(couponJpaRepository.save(any())).thenReturn(coupon);
    when(couponRedisRepository.save(any())).thenThrow(new RuntimeException());
    ResultCode resultCode = couponService.create(couponDto);
    // then
    assertThat(resultCode).isEqualTo(ResultCode.FAIL);
  }


}
