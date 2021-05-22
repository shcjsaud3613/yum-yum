package com.yumyum.domain.map.exception;

import com.yumyum.global.error.exception.BusinessException;
import com.yumyum.global.error.exception.ErrorCode;

public class PlaceNotFoundException extends BusinessException {

    public PlaceNotFoundException(final Long id){
        super("Cannot find Place where id=" + id, ErrorCode.ENTITY_NOT_FOUND);
    }

    public PlaceNotFoundException(final String email){
        super("Cannot find Place where email=" + email, ErrorCode.ENTITY_NOT_FOUND);
    }
}
