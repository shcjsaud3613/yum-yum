package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dto.FileDto;
import com.yumyum.global.common.aws.Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadService {

    final String savePath = "https://yumyum-resource.s3.ap-northeast-2.amazonaws.com/"; // S3 외부 접근 경로

    private final Uploader uploader;
    private final FileThumbnailService fileThumbnailService;

    public String uploadImage(final MultipartFile file, final String dirName) throws IOException {
        final String imagePath = uploader.upload(file, dirName);
        return imagePath;
    }

    public FileDto uploadMedia(final MultipartFile file, final String dirName) throws IOException{
        final String videoPath = uploader.upload(file, dirName);
        final String thumbnailPath = fileThumbnailService.createThumbnail(file, dirName);
        return new FileDto(videoPath, thumbnailPath);
    }
}
