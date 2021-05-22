package com.yumyum.global.error.exception;

public class InvalidParameterException extends BusinessException {

    public InvalidParameterException(final String message) {
        super(message, ErrorCode.INVALID_PARAMETER);
    }
}
