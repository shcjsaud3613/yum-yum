package com.yumyum.domain.user.dto;

import com.yumyum.domain.user.entity.Follow;
import com.yumyum.domain.user.entity.User;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowRequest {

    private Long hostId;

    private Long followerId;

    public Follow toEntity(final User host, final User follower){
        return Follow.builder()
                .host(host)
                .follower(follower)
                .build();
    }
}
