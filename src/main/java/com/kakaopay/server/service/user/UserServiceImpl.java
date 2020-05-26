package com.kakaopay.server.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.kakaopay.server.domain.common.CommonResponseDto;
import com.kakaopay.server.domain.common.Result;
import com.kakaopay.server.domain.coupon.ResultCode;
import com.kakaopay.server.domain.user.dto.UserDto;
import com.kakaopay.server.domain.user.entity.User;
import com.kakaopay.server.repository.user.UserJpaRepository;
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

  @Value("spring.jwt.secret")
  private String secretKey;
  private Date EXPIRED_TIME = new Date(System.currentTimeMillis() + 1000 * 10);
  final private String ISUSER = "joyworld007";

  final UserJpaRepository userJpaRepository;

  @Override
  public CommonResponseDto signUp(UserDto userDto) {
    try {
      //중복 체크
      if (verifyDuplicatedUser(userDto.getUserId())) {
        CommonResponseDto.builder().message(ResultCode.DUPLICATED_USER.toString());
      }
      userDto.setToken(createToken(userDto.getUserId()));
      userDto.setPassword(encrypt(userDto.getPassword()));
      userJpaRepository.save(User.ofDto(userDto));
    } catch (Exception e) {
      CommonResponseDto.builder().message(ResultCode.FAIL.toString());
    }
    return CommonResponseDto.builder().result(Result.builder().entry(userDto).build()).build();
  }

  @Override
  public CommonResponseDto signIn(String userId, String password) {
    Optional<User> user = userJpaRepository.findById(userId);
    try {
      if (user.isPresent()) {
        //비밀번호 체크
        if (encrypt(password).equals(user.get().getPassword())) {
          CommonResponseDto.builder().message(ResultCode.LOGIN_FAIL.toString());
        }
      }
    } catch (Exception e) {
      CommonResponseDto.builder().message(ResultCode.FAIL.toString());
    }

    return user.isPresent() ? CommonResponseDto.builder().build() : null;
  }

  public String createToken(String userId) {
    return JWT.create()
        .withIssuer(ISUSER)
        .withExpiresAt(EXPIRED_TIME)
        .withClaim("userId", userId)
        .sign(Algorithm.HMAC256(secretKey));
  }

  public void verifyToken(String givenToken, String userId) {
    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
        .withIssuer(ISUSER)
        .withClaim("userId", userId)
        .build();

    verifier.verify(givenToken);
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
    return Optional.ofNullable(userJpaRepository.findById(userId)).isPresent();
  }


}
