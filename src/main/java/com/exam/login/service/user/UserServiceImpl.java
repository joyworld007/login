package com.exam.login.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.exam.login.domain.common.CommonResponseDto;
import com.exam.login.domain.common.Result;
import com.exam.login.domain.common.ResultCode;
import com.exam.login.domain.user.dto.UserDto;
import com.exam.login.domain.user.entity.User;
import com.exam.login.repository.user.UserJpaRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  final UserJpaRepository userJpaRepository;
  final private String ISUSER = "joyworld007";
  @Value("spring.jwt.secret")
  private String secretKey;

  @Override
  public CommonResponseDto signUp(UserDto userDto) {
    try {
      //중복 체크
      if (verifyDuplicatedUser(userDto.getUserId())) {
        return CommonResponseDto.builder().message(ResultCode.DUPLICATED_USER.toString()).build();
      }
      userDto.setToken(createToken(userDto.getUserId()));
      userDto.setPassword(encrypt(userDto.getPassword()));
      userJpaRepository.save(User.ofDto(userDto));
    } catch (Exception e) {
      return CommonResponseDto.builder().message(ResultCode.FAIL.toString()).build();
    }
    return CommonResponseDto.builder()
        .message(ResultCode.SUCCESS.toString())
        .result(Result.builder().entry(userDto).build()).build();
  }

  @Override
  public CommonResponseDto signIn(String userId, String password) {
    Optional<User> user = userJpaRepository.findById(userId);
    try {
      if (user.isPresent()) {
        //비밀번호 체크
        if (!encrypt(password).equals(user.get().getPassword())) {
          return CommonResponseDto.builder().message(ResultCode.LOGIN_FAIL.toString()).build();
        }
        //로그인 성공 토큰 재 발급
        user.get().setToken(createToken(userId));
        userJpaRepository.save(user.get());
      } else {
        return CommonResponseDto.builder().message(ResultCode.USER_NOT_FOUND.toString()).build();
      }
    } catch (Exception e) {
      return CommonResponseDto.builder().message(ResultCode.FAIL.toString()).build();
    }
    return CommonResponseDto.builder()
        .message(ResultCode.SUCCESS.toString())
        .result(Result.builder().entry(user).build()).build();
  }

  public String createToken(String userId) {
    return JWT.create()
        .withIssuer(ISUSER)
        .withExpiresAt(new Date(System.currentTimeMillis() + 60000 * 5))
        .withIssuedAt(new Date())
        .sign(Algorithm.HMAC256(secretKey));
  }

  @Override
  public boolean verifyToken(String givenToken, String userId) {
    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
        .withIssuer(ISUSER)
        .build();
    try {
      verifier.verify(givenToken);
      return true;
    } catch (JWTVerificationException e) {
      return false;
    }
  }

  @Override
  public CommonResponseDto findById(String id) {
    Optional<User> user = userJpaRepository.findById(id);
    if (user.isPresent()) {
      return CommonResponseDto.builder()
          .result(Result.builder().entry(UserDto.ofEntity(user.get())).build())
          .message(ResultCode.FAIL.toString()).build();

    } else {
      return CommonResponseDto.builder().message(ResultCode.USER_NOT_FOUND.toString()).build();
    }
  }

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

  private boolean verifyDuplicatedUser(String userId) {
    return userJpaRepository.findById(userId).isPresent();
  }


}
