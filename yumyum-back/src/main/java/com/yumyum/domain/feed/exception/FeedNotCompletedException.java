package com.yumyum.domain.feed.exception;

import com.yumyum.global.error.exception.BusinessException;
import com.yumyum.global.error.exception.ErrorCode;

public class FeedNotCompletedException extends BusinessException {

    public FeedNotCompletedException() {
        super("Feed is not completed", ErrorCode.BAD_REQUEST);
    }
}
