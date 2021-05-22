package com.yumyum.domain.user.dto;

import com.yumyum.domain.user.entity.User;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Data
@Valid
@ToString
public class SignUpRequest {

    private String email;

    private String nickname;

    private String introduction;

    private String profilePath;

    public User toEntity(final LocalDateTime nowTime){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .introduction(introduction)
                .profilePath(profilePath)
                .createdDate(nowTime)
                .modifiedDate(nowTime)
                .build();
    }
}