package com.kakaopay.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.kakaopay.server"})
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }

  /**
   * TODO : API 구현하기
   * - 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관 ( 완료 )
   * - 생성된 쿠폰중 하나를 사용자에게 지급
   * - 사용자에게 지급된 쿠폰을 조회
   * - 지급된 쿠폰중 하나를 사용  (쿠폰 재사용은 불가) - 지급된 쿠폰중 하나를 사용 취소 (취소된 쿠폰 재사용 가능)
   * - 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
   *
   * TODO : REDIS 를 이용하여 CQRS 구현하기
   * - EMBED REDIS 연동하기
   * - JWT 웹 토큰을 이용한 회원가입, 로그인, API 인증 구현
   * - 만료 3일전 사용자에게 메시지 보내기
   * - nGrinder 성능 테스트 결과
   * - 10만개 csv 이상 벌크 insert
   */

}
