package com.yumyum.domain.feed.dto;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimilarityDto{

    private Long feedId;

    private Double similarity;
}
