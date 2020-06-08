package com.kakaopay.server.controller;

import com.kakaopay.server.domain.common.CommonResponseDto;
import com.kakaopay.server.domain.common.CommonResponseEntity;
import com.kakaopay.server.domain.user.dto.UserDto;
import com.kakaopay.server.service.user.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("spring.jwt.secret")
  private String secretKey;

  @PostMapping("/signup")
  public ResponseEntity signup(@RequestBody UserDto userDto)
      throws Exception {
    String userId = userDto.getUserId();
    String password = userDto.getPassword();
    if (!Optional.ofNullable(userId).isPresent() || !Optional.ofNullable(password).isPresent()) {
      return CommonResponseEntity.badRequest();
    }
    CommonResponseDto<UserDto> commonResponseDto = userService.signUp(userDto);
    switch (commonResponseDto.getMessage()) {
      case "DUPLICATED_USER":
        return CommonResponseEntity.fail("FAIL", commonResponseDto.getMessage());
    }
    return CommonResponseEntity.ok(commonResponseDto.getResult());
  }

  @PostMapping("/signin")
  public ResponseEntity signin(@RequestBody UserDto userDto) {
    String userId = userDto.getUserId();
    String password = userDto.getPassword();
    if (!Optional.ofNullable(userId).isPresent() || !Optional.ofNullable(password).isPresent()) {
      return CommonResponseEntity.badRequest();
    }
    CommonResponseDto<UserDto> commonResponseDto = userService.signIn(userId, password);
    switch (commonResponseDto.getMessage()) {
      case "LOGIN_FAIL":
      case "USER_NOT_FOUND":
        return CommonResponseEntity.fail("FAIL", commonResponseDto.getMessage());
    }
    return CommonResponseEntity.ok(commonResponseDto.getResult());
  }

}

