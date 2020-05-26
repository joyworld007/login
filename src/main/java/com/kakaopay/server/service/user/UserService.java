package com.kakaopay.server.service.user;

import com.kakaopay.server.domain.common.CommonResponseDto;
import com.kakaopay.server.domain.coupon.ResultCode;
import com.kakaopay.server.domain.user.dto.UserDto;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  public CommonResponseDto signUp(UserDto userDto);
  public CommonResponseDto signIn(String userId, String password);
}
