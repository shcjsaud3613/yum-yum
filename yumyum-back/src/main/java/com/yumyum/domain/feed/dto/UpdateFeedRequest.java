package com.yumyum.domain.feed.dto;

import com.yumyum.domain.map.dto.PlaceRequest;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateFeedRequest {

    private Long id;

    private String title;

    private String content;

    private Long score;

    private PlaceRequest placeRequest;

    private Boolean isCompleted;
}
