package com.yumyum.domain.map.exception;

import com.yumyum.global.error.exception.DuplicateParameterException;

public class PlaceDuplicateException extends DuplicateParameterException {

    public PlaceDuplicateException(){
        super("Place");
    }
}
