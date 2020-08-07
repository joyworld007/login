package com.exam.login.interceptor;

import com.exam.login.domain.user.entity.User;
import com.exam.login.repository.user.UserJpaRepository;
import com.exam.login.service.user.UserService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

  final static String HEADER_USER_ID = "userID";
  static final String HEADER_STRING = "Authorization";
  static final String TOKEN_PREFIX = "Bearer ";
  final private UserService userService;
  final private UserJpaRepository userJpaRepository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if (!Optional.ofNullable(request.getHeader(HEADER_USER_ID)).isPresent()) {
      response.getWriter().write("Header userId Must be not null");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return false;
    }
    String userId = request.getHeader(HEADER_USER_ID);

    if (!Optional.ofNullable(request.getHeader(HEADER_STRING)).isPresent()) {
      response.getWriter().write("Header Authorization Must be not null");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return false;
    }

    String givenToken = request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "");

    Optional<User> user = userJpaRepository.findById(userId);
    if (!user.isPresent()) {
      response.getWriter().write("Not Found User");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return false;
    }
    String userToken = user.get().getToken();

    //사용자 토큰인지 체
    if (!givenToken.equals(userToken)) {
      response.getWriter().write("Invalid Token");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return false;
    }
    //토큰 유효성 체크
    if (!userService.verifyToken(givenToken, userId)) {
      response.getWriter().write("Invalid Token");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return false;
    }
    return true;
  }
}
