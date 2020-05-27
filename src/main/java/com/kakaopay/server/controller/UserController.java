package com.kakaopay.server.controller;

import com.kakaopay.server.domain.common.CommonResponseDto;
import com.kakaopay.server.domain.common.CommonResponseEntity;
import com.kakaopay.server.domain.user.dto.UserDto;
import com.kakaopay.server.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

  final UserService userService;

  @Value("spring.jwt.secret")
  private String secretKey;

  @PostMapping("/signUp")
  public ResponseEntity signUp(@RequestBody UserDto userDto)
      throws Exception {
    CommonResponseDto<UserDto> commonResponseDto = userService.signUp(userDto);
    return CommonResponseEntity.ok(commonResponseDto.getResult());
  }

  @GetMapping("/signIn")
  public ResponseEntity signIn(
      @RequestParam(name = "userId", required = true) String userId
      , @RequestParam(name = "password", required = true) String password
  ) {
    CommonResponseDto<String> commonResponseDto = userService.signIn(userId, password);
    return CommonResponseEntity.ok(commonResponseDto.getResult());
  }

}

