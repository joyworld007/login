package com.exam.login.controller;

import com.exam.login.domain.common.CommonResponseDto;
import com.exam.login.domain.common.CommonResponseEntity;
import com.exam.login.domain.user.dto.UserDto;
import com.exam.login.service.user.UserService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
@SuppressWarnings("unchecked")
public class UserController {

  final UserService userService;

  @Value("spring.jwt.secret")
  private String secretKey;

  @PostMapping("/join")
  public ResponseEntity signup(@RequestBody UserDto userDto)
      throws Exception {
    String userId = userDto.getUserId();
    String password = userDto.getPassword();
    if (!Optional.ofNullable(userId).isPresent() || !Optional.ofNullable(password).isPresent()) {
      return CommonResponseEntity.badRequest();
    }
    userDto.setRegistDate(LocalDateTime.now());
    CommonResponseDto<UserDto> commonResponseDto = userService.signUp(userDto);
    switch (commonResponseDto.getMessage()) {
      case "DUPLICATED_USER":
        return CommonResponseEntity.fail("FAIL", commonResponseDto.getMessage());
    }
    return CommonResponseEntity.ok(commonResponseDto.getResult());
  }

  @PostMapping("/login")
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
    // 최근 접속 일자 업데이트
    userService.modifyLastLoginDate(userDto.getUserId());
    return CommonResponseEntity.ok(commonResponseDto.getResult());
  }

  @GetMapping("/info")
  public ResponseEntity into(@RequestHeader("userId") String userId) {
    CommonResponseDto<UserDto> commonResponseDto = userService.findById(userId);
    switch (commonResponseDto.getMessage()) {
      case "USER_NOT_FOUND":
        return CommonResponseEntity.unauthorized();
    }
    return CommonResponseEntity.ok(commonResponseDto.getResult());
  }

}

