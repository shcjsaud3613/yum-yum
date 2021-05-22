package com.yumyum.domain.user.exception;

import com.yumyum.global.error.exception.BusinessException;
import com.yumyum.global.error.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(final Long id){
        super("Cannot find User where id=" + id, ErrorCode.ENTITY_NOT_FOUND);
    }

    public UserNotFoundException(final String email){
        super("Cannot find User where email=" + email, ErrorCode.ENTITY_NOT_FOUND);
    }
}