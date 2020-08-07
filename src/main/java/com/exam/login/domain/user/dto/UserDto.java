package com.exam.login.domain.user.dto;


import com.exam.login.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

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

  //유저 이름 이름
  private String name;

  //토큰
  private String token;

  //마지막 로그인 일자
  private LocalDateTime lastLoginDate;

  //생성일
  private LocalDateTime registDate;

  protected UserDto(User entity) {
    this.userId = entity.getUserId();
    this.password = entity.getPassword();
    this.token = entity.getToken();
    this.name = entity.getName();
    this.lastLoginDate = entity.getLastLoginDate();
    this.registDate = entity.getRegistDate();
  }

  public static UserDto ofEntity(User entity) {
    return new UserDto(entity);
  }


}
