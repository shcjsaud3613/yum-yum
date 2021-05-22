package com.yumyum.domain.feed.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class FileUploadService {

    public String upload(final MultipartFile file, final String uploadPath){ // 지정된 파일경로에 파일 저장 (동영상, 이미지)

        Date date = new Date();
        StringBuilder sb = new StringBuilder();

        // file image 가 없을 경우
        if (file.isEmpty()) {
            sb.append("none");
        } else {
            sb.append(date.getTime());
            sb.append(file.getOriginalFilename());
        }

        if (!file.isEmpty()) {
            File dest = new File(uploadPath + sb.toString());
            try {
                file.transferTo(dest);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // db에 파일 위치랑 번호 등록
        }

        return sb.toString();
    }
}
