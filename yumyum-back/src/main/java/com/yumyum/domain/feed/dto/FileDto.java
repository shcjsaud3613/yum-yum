package com.yumyum.domain.feed.dto;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDto {

    private String videoPath;

    private String thumbnailPath;
}
