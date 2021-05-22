package com.yumyum.domain.feed.exception;

import com.yumyum.global.error.exception.BusinessException;
import com.yumyum.global.error.exception.ErrorCode;

public class LikeNotFoundException extends BusinessException {

    public LikeNotFoundException(final Long feedId, final Long userId){
        super("Cannot find Follow where feedId=" + feedId + " and userId=" + userId, ErrorCode.ENTITY_NOT_FOUND);
    }
}
