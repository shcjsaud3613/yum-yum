package com.yumyum.global.common.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ResponseEntity<BasicResponse> makeResponse(String status, Object data, String message,
                                                             HttpStatus httpStatus) {
        BasicResponse result = BasicResponse.builder().status(status).message(message).data(data).build();
        return new ResponseEntity<>(result, httpStatus);
    }

    public static String convertObjToJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Failed convert object to json";
        }
    }
}
