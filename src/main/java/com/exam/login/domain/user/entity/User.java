package com.exam.login.domain.user.entity;

import com.exam.login.domain.user.dto.UserDto;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class User {

  // 유저 아이디
  @Id
  private String userId;

  //패스워드
  private String password;

  //이름
  private String name;

  //토큰
  private String token;

  //마지막 로그인 시간
  private LocalDateTime lastLoginDate;

  //가입일자
  private LocalDateTime registDate;

  @Builder
  public User(String userId, String password, String token, String name) {
    this.userId = userId;
    this.password = password;
    this.token = token;
    this.name = name;
  }

  protected User(UserDto dto) {
    this.userId = dto.getUserId();
    this.password = dto.getPassword();
    this.token = dto.getToken();
    this.name = dto.getName();
    this.lastLoginDate = dto.getLastLoginDate();
    this.registDate = dto.getRegistDate();
  }

  public static User ofDto(UserDto dto) {
    return new User(dto);
  }

}
