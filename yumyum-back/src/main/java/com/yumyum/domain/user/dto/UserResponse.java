package com.yumyum.domain.user.dto;

import com.yumyum.domain.user.entity.User;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String nickname;
    private String introduction;
    private String profilePath;

    public UserResponse(final User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.profilePath = user.getProfilePath();
    }
}
