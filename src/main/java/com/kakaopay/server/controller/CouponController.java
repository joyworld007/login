package com.kakaopay.server.controller;

import com.kakaopay.server.domain.common.CommonResponseDto;
import com.kakaopay.server.domain.common.CommonResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/coupons")
public class CouponController {

    @PostMapping()
    public ResponseEntity<?> createCoupon() throws Exception {
        return CommonResponseEntity.created();
    }



}
