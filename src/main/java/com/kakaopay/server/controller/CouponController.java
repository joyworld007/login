package com.kakaopay.server.controller;

import com.kakaopay.server.domain.common.CommonResponseEntity;
import com.kakaopay.server.domain.common.Result;
import com.kakaopay.server.domain.coupon.ResultCode;
import com.kakaopay.server.domain.coupon.dto.CouponDto;
import com.kakaopay.server.domain.coupon.entity.Coupon;
import com.kakaopay.server.service.coupon.CouponService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/coupons")
public class CouponController {

  @Autowired
  CouponService couponService;

  @PostMapping
  public ResponseEntity createCoupon(@RequestBody final Map<String, String> payload)
      throws Exception {
    if (Optional.ofNullable(payload).isPresent()) {
      log.info("createCoupon - size : {}", payload.get("size"));
      couponService.creat(Long.parseLong(payload.get("size")));
    }
    return CommonResponseEntity.created();
  }

  @PutMapping("/{id}")
  public ResponseEntity issueCoupon(@RequestBody final CouponDto couponDto,
      @PathVariable(value = "id") Long id) throws Exception {
    if (Optional.ofNullable(couponDto).isPresent()) {
      ResultCode resultcode = couponService.issueCoupon(id, couponDto);
      //만료된 쿠폰을 지급 하려 할 경우
      if ("COUPON_EXPIRED".equals(resultcode.toString())) {
        return CommonResponseEntity.fail(
            ResultCode.COUPON_EXPIRED.toString(), "Coupon is Expired"
        );
      }
    }
    return CommonResponseEntity.ok();
  }

  @GetMapping
  public ResponseEntity findCouponByUserId(
      @RequestParam(name="userId", required = true) String userId) throws Exception {
    Result<List<CouponDto>> result = couponService.findCouponByUserId(userId);
    log.info("couponList : {}", result.getEntry().size());
    return CommonResponseEntity.ok(result);
  }


}
