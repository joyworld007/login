package com.kakaopay.server.domain.common;

import lombok.Builder;

@Builder
public class Result<T> {
    private T entry;
}
