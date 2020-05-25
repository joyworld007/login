package com.kakaopay.server.controller;

import com.kakaopay.server.domain.common.CommonResponseEntity;
import com.kakaopay.server.domain.coupon.dto.CouponDto;
import com.kakaopay.server.domain.user.dto.UserDto;
import com.kakaopay.server.service.user.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

  final UserService userService;

  @PostMapping
  public ResponseEntity create(@RequestBody UserDto userDto)
      throws Exception {
    userService.create(userDto);
    return CommonResponseEntity.created();
  }

}
