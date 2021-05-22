package com.yumyum.domain.feed.exception;

import com.yumyum.global.error.exception.DuplicateParameterException;

public class LikeDuplicateException extends DuplicateParameterException {

    public LikeDuplicateException(){
        super("Like");
    }
}
