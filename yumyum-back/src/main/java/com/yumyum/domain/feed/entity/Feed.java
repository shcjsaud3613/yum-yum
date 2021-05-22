package com.yumyum.domain.feed.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yumyum.domain.feed.dto.UpdateFeedRequest;
import com.yumyum.domain.map.entity.Place;
import com.yumyum.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "feeds")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content", length = 200)
    private String content;

    @Column(name = "score")
    private Long score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(name = "video_path")
    private String videoPath;

    @Column(name = "thumbnail_path")
    private String thumbnailPath;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    public void updateFeed(final UpdateFeedRequest dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.score = dto.getScore();
        this.isCompleted = dto.getIsCompleted();
        this.modifiedDate = LocalDateTime.now();
    }

    public void updateFeed(final UpdateFeedRequest dto, final Place place){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.score = dto.getScore();
        this.place = place;
        this.isCompleted = dto.getIsCompleted();
        this.modifiedDate = LocalDateTime.now();
    }
}