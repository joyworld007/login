package com.kakaopay.server.controller;

import com.kakaopay.server.domain.common.CommonResponseEntity;
import com.kakaopay.server.service.coupon.CouponService;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/coupons")
public class CouponController {

  @Autowired
  CouponService couponService;

  @PostMapping
  @ResponseBody
  public ResponseEntity createCoupon(@RequestBody final Map<String, String> payload)
      throws Exception {
    long size;
    if (Optional.ofNullable(payload).isPresent()) {
      couponService.creat(Long.parseLong(payload.get("size")));
    }
    return CommonResponseEntity.created();
  }


}
