package com.yumyum.global.error.exception;

public class DuplicateParameterException extends BusinessException{

    public DuplicateParameterException(final String value) {
        super(value + " is Duplicated", ErrorCode.ENTITY_DUPLICATE);
    }
}
