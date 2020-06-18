package com.shyun.kr.controller;

import com.shyun.kr.domain.common.CommonResponseEntity;
import com.shyun.kr.domain.common.Result;
import com.shyun.kr.domain.common.ResultCode;
import com.shyun.kr.domain.coupon.dto.CouponDto;
import com.shyun.kr.service.coupon.CouponService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@RequestMapping("/v1/coupons")
public class CouponController {

  private final CouponService couponService;

  @PostMapping("generate")
  public ResponseEntity generate(@RequestBody final Map<String, String> payload)
      throws Exception {
    if (Optional.ofNullable(payload).isPresent()) {
      couponService.generate(Long.parseLong(payload.get("size")));
    }
    return CommonResponseEntity.created();
  }

  @PostMapping("generate/csv")
  public ResponseEntity generate()
      throws Exception {
    couponService.generateCsv();
    return CommonResponseEntity.created();
  }

  @PostMapping
  public ResponseEntity create(@RequestBody CouponDto couponDto)
      throws Exception {
    couponService.create(couponDto);
    return CommonResponseEntity.created();
  }

  @GetMapping("/{id}")
  public ResponseEntity getCoupon(@PathVariable(value = "id") Long id) throws Exception {
    Result<CouponDto> result = couponService.findById(id);
    if (Optional.ofNullable(result.getEntry()).isPresent()) {
      return CommonResponseEntity.ok(result);
    } else {
      return CommonResponseEntity.notFound();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity updateCoupon(@RequestBody final CouponDto couponDto,
      @PathVariable(value = "id") Long id) throws Exception {
    if (Optional.ofNullable(couponDto).isPresent()) {
      ResultCode resultcode = couponService.updateCoupon(id, couponDto);
      switch (resultcode.toString()) {
        case "BAD_REQUEST":
          return CommonResponseEntity.badRequest();
        case "COUPON_EXPIRED":
          return CommonResponseEntity.fail(
              ResultCode.COUPON_EXPIRED.toString(), "Coupon is Expired"
          );
        case "COUPON_NOT_FOUND":
          return CommonResponseEntity.notFound();
      }
    }
    return CommonResponseEntity.ok();
  }

  @GetMapping
  public ResponseEntity getCouponByUserId(
      @RequestParam(name = "userId", required = false, defaultValue = "") String userId
      , Pageable pageable) throws Exception {
    Result<List<CouponDto>> result = couponService.findCouponByUserId(userId, pageable);
    return CommonResponseEntity.ok(result);
  }

  @GetMapping("/today-expired-coupons")
  public ResponseEntity getTodayExpireCoupon(Pageable pageable) throws Exception {
    Result<List<CouponDto>> result = couponService.findTodayExpiredCoupon(pageable);
    return CommonResponseEntity.ok(result);
  }

  @GetMapping("/notify-expire-coupons")
  public ResponseEntity notifyExpireCoupons(
      @RequestParam(name = "day", required = true, defaultValue = "3") Long day
  ) throws Exception {
    couponService.notifyExpireCoupon(day);
    return CommonResponseEntity.ok();
  }

}
