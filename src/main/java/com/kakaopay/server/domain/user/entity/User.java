package com.kakaopay.server.domain.user.entity;

import com.kakaopay.server.domain.user.dto.UserDto;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

  // 유저 아이디
  @Id
  private String userId;

  //패스워드
  private String pass;

  //발급받은 토큰
  private String token;

  protected User(UserDto dto) {
    this.userId = dto.getUserId();
    this.pass = dto.getPass();
    this.token = dto.getToken();
  }

  public static User ofDto(UserDto dto) {
    return new User(dto);
  }

}
