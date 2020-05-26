package com.kakaopay.server.service.user;

import com.kakaopay.server.domain.coupon.ResultCode;
import com.kakaopay.server.domain.user.dto.UserDto;
import com.kakaopay.server.domain.user.entity.User;
import com.kakaopay.server.repository.user.UserJpaRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  final UserJpaRepository userJpaRepository;

  @Override
  public ResultCode create(UserDto userDto) {
    try {
      userDto.setPass(encrypt(userDto.getPass()));
      userJpaRepository.save(User.ofDto(userDto));
    } catch (Exception e) {
      return ResultCode.FAIL;
    }
    return ResultCode.SUCCESS;
  }

  @Override
  public UserDto findByUserId(String userId) {
    Optional<User> user = userJpaRepository.findById(userId);
    return user.isPresent() ? UserDto.ofEntity(user.get()) : null;
  }

  @Override
  public UserDto findByUserIdAndPass(String userId, String pass) {
    try {
      Optional<User> user = userJpaRepository.findByUserIdAndPass(userId, encrypt(pass));
      return user.isPresent() ? UserDto.ofEntity(user.get()) : null;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public String encrypt(String input) throws NoSuchAlgorithmException {
    String output = "";
    StringBuffer sb = new StringBuffer();
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    md.update(input.getBytes());
    byte[] msgb = md.digest();
    for (int i = 0; i < msgb.length; i++) {
      byte temp = msgb[i];
      String str = Integer.toHexString(temp & 0xFF);
      while (str.length() < 2) {
        str = "0" + str;
      }
      str = str.substring(str.length() - 2);
      sb.append(str);
    }
    output = sb.toString();
    return output;
  }

}
