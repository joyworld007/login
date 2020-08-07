package com.exam.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  @Qualifier(value = "jwtAuthInterceptor")
  private HandlerInterceptor interceptor;

  private String[] INTERCEPTOR_WHITE_LIST = {
      "/v1/member/join/**",
      "/v1/member/login/**",
      "/v2/api-docs",
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/webjars/**"
  };

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(interceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(INTERCEPTOR_WHITE_LIST);
  }

}
