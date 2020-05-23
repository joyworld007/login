package com.kakaopay.server.domain.common;

import lombok.Builder;

@Builder
public class CommonResponseDto<T> {
    private Result<T> result;
    private String code;
    private String message;
}
