package com.kakaopay.server.service.user;

import com.kakaopay.server.domain.coupon.ResultCode;
import com.kakaopay.server.domain.user.dto.UserDto;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  public ResultCode create(UserDto userDto);
  public UserDto findByUserId(String userId);
  public String encrypt(String input) throws NoSuchAlgorithmException;
}
