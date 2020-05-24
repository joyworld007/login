package com.kakaopay.server.controller;

import com.kakaopay.server.domain.common.CommonResponseEntity;
import com.kakaopay.server.domain.common.Result;
import com.kakaopay.server.domain.coupon.ResultCode;
import com.kakaopay.server.domain.coupon.dto.CouponDto;
import com.kakaopay.server.service.coupon.CouponService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity updateCoupon(@RequestBody final CouponDto couponDto,
      @PathVariable(value = "id") Long id) throws Exception {
    if (Optional.ofNullable(couponDto).isPresent()) {
      ResultCode resultcode = couponService.updateCoupon(id, couponDto);
      //잘못된 요청
      if ("BAD_REQUEST".equals(resultcode.toString())) {
        return CommonResponseEntity.badRequest();
      }
      //만료된 쿠폰
      if ("COUPON_EXPIRED".equals(resultcode.toString())) {
        return CommonResponseEntity.fail(
            ResultCode.COUPON_EXPIRED.toString(), "Coupon is Expired"
        );
      }
      //쿠폰을 찾을수 없다.
      if ("COUPON_NOT_FOUND".equals(resultcode.toString())) {
        return CommonResponseEntity.notFound();
      }

    }
    return CommonResponseEntity.ok();
  }

  @GetMapping
  public ResponseEntity findCouponByUserId(
      @RequestParam(name = "userId", required = false, defaultValue = "") String userId
      , Pageable pageable) throws Exception {
    Result<List<CouponDto>> result = couponService.findCouponByUserId(userId, pageable);
    log.info("couponList : {}", result.getEntry().size());
    return CommonResponseEntity.ok(result);
  }

  @GetMapping("/today-expired-coupons")
  public ResponseEntity findTodayExpireCoupon(Pageable pageable) throws Exception {
    Result<List<CouponDto>> result = couponService.findTodayExpiredCoupon(pageable);
    log.info("couponList : {}", result.getEntry().size());
    return CommonResponseEntity.ok(result);
  }
}
