package com.kakaopay.server.domain.user.entity;

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
  private String id;

  //패스워드
  private String pass;

  //발급받은 토큰
  private String token;

}
