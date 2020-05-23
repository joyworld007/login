package com.kakaopay.server.domain.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommonResponseEntity {

    public static ResponseEntity<CommonResponseDto> ok() {
        CommonResponseDto commonResponseDto = CommonResponseDto.builder()
                .code("SUCCESS")
                .message("OK")
                .build();
        return new ResponseEntity(commonResponseDto, HttpStatus.OK);
    }

    public static ResponseEntity<CommonResponseDto> ok(Result result) {
        CommonResponseDto commonResponseDto = CommonResponseDto.builder()
                .result(result)
                .code("SUCCESS")
                .message("OK")
                .build();
        return new ResponseEntity(commonResponseDto, HttpStatus.OK);
    }

    public static ResponseEntity<CommonResponseDto> ok(Result result, String code, String message) {
        CommonResponseDto commonResponseDto = CommonResponseDto.builder()
                .result(result)
                .code(code)
                .message(message)
                .build();
        return new ResponseEntity(commonResponseDto, HttpStatus.OK);
    }

    public static ResponseEntity<CommonResponseDto> created() {
        CommonResponseDto commonResponseDto = CommonResponseDto.builder()
                .code("SUCCESS")
                .message("OK")
                .build();
        return new ResponseEntity(commonResponseDto, HttpStatus.CREATED);
    }

    public static ResponseEntity<CommonResponseDto> badRequest(String code, String message) {
        CommonResponseDto commonResponseDto = CommonResponseDto.builder()
                .code(code)
                .message(message)
                .build();
        return new ResponseEntity(commonResponseDto, HttpStatus.BAD_REQUEST);
    }
}
