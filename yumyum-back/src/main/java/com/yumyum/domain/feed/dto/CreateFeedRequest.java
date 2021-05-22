package com.yumyum.domain.feed.dto;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.map.dto.PlaceRequest;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.entity.User;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFeedRequest {

    private String title;

    private String content;

    private Long score;

    private Long userId;

    private PlaceRequest placeRequest;

    private String videoPath;

    private String thumbnailPath;

    private Boolean isCompleted;

    public Feed toEntity(final User user, final Place place){
        return Feed.builder()
                .title(title)
                .score(score)
                .content(content)
                .user(user)
                .place(place)
                .videoPath(videoPath)
                .thumbnailPath(thumbnailPath)
                .isCompleted(isCompleted)
                .build();
    }
}
