package com.yumyum.domain.user.exception;

import com.yumyum.global.error.exception.BusinessException;
import com.yumyum.global.error.exception.ErrorCode;

public class FollowNotFountException extends BusinessException {

    public FollowNotFountException(final Long hostId, final Long followId){
        super("Cannot find Follow where hostId=" + hostId + " and followerId=" + followId, ErrorCode.ENTITY_NOT_FOUND);
    }
}
