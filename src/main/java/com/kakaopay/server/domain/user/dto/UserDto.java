package com.kakaopay.server.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

  // 유저 아이디
  private String userId;

  //패스워드
  private String pass;

  //발급받은 토큰
  private String token;

}
