package com.yumyum.domain.user.exception;

import com.yumyum.global.error.exception.DuplicateParameterException;

public class EmailDuplicateException extends DuplicateParameterException {

    public EmailDuplicateException(){
        super("Email");
    }
}
