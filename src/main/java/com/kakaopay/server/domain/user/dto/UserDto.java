package com.kakaopay.server.domain.user.dto;


import com.kakaopay.server.domain.user.entity.User;
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
  private String password;

  //토큰
  private String token;

  protected UserDto(User entity) {
    this.userId = entity.getUserId();
    this.password = entity.getPassword();
    this.token = entity.getToken();
  }

  public static UserDto ofEntity(User entity) {
    return new UserDto(entity);
  }


}
