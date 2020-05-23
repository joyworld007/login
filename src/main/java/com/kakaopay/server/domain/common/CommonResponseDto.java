package com.kakaopay.server.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonIgnoreProperties
public class CommonResponseDto<T> {

  private Result<T> result;
  private String code;
  private String message;
}
