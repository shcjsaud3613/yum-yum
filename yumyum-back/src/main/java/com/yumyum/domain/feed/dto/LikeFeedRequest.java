package com.yumyum.domain.feed.dto;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.feed.entity.Like;
import com.yumyum.domain.user.entity.User;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LikeFeedRequest {

    private Long feedId;

    private Long userId;

    public Like toEntity(final Feed feed, final User user){
        return Like.builder()
                .feed(feed)
                .user(user)
                .build();
    }
}
