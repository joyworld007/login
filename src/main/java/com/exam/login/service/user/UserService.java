package com.exam.login.service.user;

import com.exam.login.domain.common.CommonResponseDto;
import com.exam.login.domain.user.dto.UserDto;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  //회원 가입
  public CommonResponseDto signUp(UserDto userDto);

  //로그인
  public CommonResponseDto signIn(String userId, String password);

  //토큰 유효성 검사
  public boolean verifyToken(String givenToken, String userId);

  //회원 정보 조회
  public CommonResponseDto findById(String id);

  //로그인 일자 업데이트
  @Transactional
  public void modifyLastLoginDate(String id);

}
