package com.kakaopay.server.domain.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unchecked")
public class CommonResponseEntity {

  public static ResponseEntity ok() {
    CommonResponseDto commonResponseDto = CommonResponseDto.builder()
        .code("SUCCESS")
        .message("OK")
        .build();
    return new ResponseEntity(commonResponseDto, HttpStatus.OK);
  }

  public static ResponseEntity ok(Result result) {
    CommonResponseDto<Result<?>> commonResponseDto = CommonResponseDto.builder()
        .result(result)
        .code("SUCCESS")
        .message("OK")
        .build();
    return new ResponseEntity(commonResponseDto, HttpStatus.OK);
  }

  public static ResponseEntity fail(String code, String message) {
    CommonResponseDto commonResponseDto = CommonResponseDto.builder()
        .code(code)
        .message(message)
        .build();
    return new ResponseEntity(commonResponseDto, HttpStatus.OK);
  }

  public static ResponseEntity created() {
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  public static ResponseEntity badRequest(String code, String message) {
    CommonResponseDto commonResponseDto = CommonResponseDto.builder()
        .code(code)
        .message(message)
        .build();
    return new ResponseEntity(commonResponseDto, HttpStatus.BAD_REQUEST);
  }

  public static ResponseEntity notFound() {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

}
