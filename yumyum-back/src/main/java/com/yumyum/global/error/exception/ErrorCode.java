package com.yumyum.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_PARAMETER(400, "C001", "Invalid Parameter"), // 변수가 없거나 유효하지 않은 값일 때
    BAD_REQUEST(400, "C002", "Bad Request"), // 입력 파라메터가 서식에 맞지 않을 때
    ENTITY_NOT_FOUND(404, "C003", "Entity Not Found"), // 객체를 찾지 못했을 때
    ENTITY_DUPLICATE(409, "C004", "Entity is Duplicated"), // 데이터가 중복되었을 때
    INTERNAL_SERVER_ERROR(500, "C005", "Server Error"), // 서버 오류일 때

    ;
    private final String code;
    private final String error;
    private int status;

    ErrorCode(final int status, final String code, final String error) {
        this.status = status;
        this.error = error;
        this.code = code;
    }

    public String getError() {
        return this.error;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}