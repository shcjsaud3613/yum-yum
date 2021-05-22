package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    //        로컬 세팅
//        final String uploadPath = "D://SSAFY/rest-api-test/";
//        final String savePath = "D://SSAFY/rest-api-test/";

    //        AWS 세팅
    final String uploadPath = "/home/ubuntu/resources/"; // AWS 업로드 경로
    final String savePath = "http://k4b206.p.ssafy.io/resources/"; // AWS 외부 접근 경로

    private final FileUploadService fileUploadService;
    private final FileThumbnailService fileThumbnailService;

    public String uploadImage(final MultipartFile file, final String folderName){
        final String imageName = fileUploadService.upload(file, uploadPath + folderName);
        return savePath + folderName + imageName;
    }

    public FileDto uploadMedia(final MultipartFile file, final String folderName){
        final String videoName = fileUploadService.upload(file, uploadPath + folderName);
        final String thumbnailName = fileThumbnailService.createThumbnail(videoName, uploadPath + folderName);
        return new FileDto(savePath + folderName + videoName, savePath + folderName + thumbnailName);
    }
}
