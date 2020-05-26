package com.kakaopay.server.domain.user.entity;

import com.kakaopay.server.domain.user.dto.UserDto;
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
public class User {

  // 유저 아이디
  @Id
  private String userId;

  //패스워드
  private String password;

  @Setter
  private String token;

  @Builder
  public User(String userId, String password, String token) {
    this.userId = userId;
    this.password = password;
    this.token = token;
  }

  protected User(UserDto dto) {
    this.userId = dto.getUserId();
    this.password = dto.getPassword();
    this.token = dto.getToken();
  }

  public static User ofDto(UserDto dto) {
    return new User(dto);
  }

}
