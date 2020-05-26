package com.kakaopay.server.controller;

import com.kakaopay.server.domain.common.CommonResponseEntity;
import com.kakaopay.server.domain.user.dto.UserDto;
import com.kakaopay.server.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @PostMapping
  public ResponseEntity create(@RequestBody UserDto userDto)
      throws Exception {
    userService.create(userDto);
    return CommonResponseEntity.created();
  }

  @GetMapping("/login")
  public ResponseEntity login(
      @RequestParam(name = "userId", required = true) String userId,
      @RequestParam(name = "pass", required = true) String pass
  )
      throws Exception {
    UserDto userDto = userService.findByUserIdAndPass(userId, pass);
    return CommonResponseEntity.ok();
  }

}
