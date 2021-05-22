package com.yumyum.global.error;

import com.yumyum.global.error.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String error;
    private String code;
    private String message;

    private ErrorResponse(final ErrorCode code, final String message) {
        this.status = code.getStatus();
        this.error = code.getError();
        this.code = code.getCode();
        this.message = message;
    }

    private ErrorResponse(final ErrorCode code) {
        this.status = code.getStatus();
        this.error = code.getError();
        this.code = code.getCode();
        this.message = "";
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final String message) {
        return new ErrorResponse(code, message);
    }
}