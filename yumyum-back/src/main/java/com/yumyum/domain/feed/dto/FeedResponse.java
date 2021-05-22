package com.yumyum.domain.feed.dto;

import com.yumyum.domain.feed.entity.Feed;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedResponse {

    private Long id;

    private String title;

    private String content;

    private Long score;

    private User user;

    private Place place;

    private String videoPath;

    private String thumbnailPath;

    private Boolean isCompleted;

    private Long likeCount;

    private Boolean isLike;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public FeedResponse(final Feed feed, final User user, final Place place, final Long likeCount, final Boolean isLike){
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.score = feed.getScore();
        this.user = user;
        this.place = place;
        this.videoPath = feed.getVideoPath();
        this.thumbnailPath = feed.getThumbnailPath();
        this.isCompleted = feed.getIsCompleted();
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.createdDate = feed.getCreatedDate();
        this.modifiedDate = feed.getModifiedDate();
    }
}
