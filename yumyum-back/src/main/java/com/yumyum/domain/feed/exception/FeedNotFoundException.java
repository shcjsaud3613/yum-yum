package com.yumyum.domain.feed.exception;

import com.yumyum.global.error.exception.BusinessException;
import com.yumyum.global.error.exception.ErrorCode;

public class FeedNotFoundException extends BusinessException {

    public FeedNotFoundException(final Long id){
        super("Cannot find Feed where id=" + id, ErrorCode.ENTITY_NOT_FOUND);
    }
}
