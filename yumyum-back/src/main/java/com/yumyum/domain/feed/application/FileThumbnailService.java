package com.yumyum.domain.feed.application;

import com.yumyum.global.common.aws.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class FileThumbnailService {

    private final S3Uploader s3Uploader;
    private final static String TEMP_FILE_PATH = "src/main/resources/";

    public String createThumbnail(final String videoName, final String uploadPath) { // 동영상에서 첫 프레임을 썸네일로 추출 및 저장
        int frameNumber = 0;
        final String thumbnailName = videoName.substring(0, videoName.length()-4) + "_thumbnail.png";
        final File video = new File(uploadPath + videoName);
        final File thumbnail = new File(uploadPath + thumbnailName);

        int thumbnail_width = 480;
        int thumbnail_height = 270;

        try {
            Picture picture = FrameGrab.getFrameFromFile(video, frameNumber);
            BufferedImage buffer_original_image = AWTUtil.toBufferedImage(picture);
            BufferedImage buffer_thumbnail_image = new BufferedImage(thumbnail_width, thumbnail_height, BufferedImage.TYPE_3BYTE_BGR);

            Graphics2D graphic = buffer_thumbnail_image.createGraphics();
            graphic.drawImage(buffer_original_image, 0, 0, thumbnail_width, thumbnail_height, null);

            ImageIO.write(buffer_thumbnail_image, "png", thumbnail);
            return thumbnailName;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (JCodecException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String createThumbnail(final MultipartFile file, final String dirName) throws IOException { // 동영상에서 첫 프레임을 썸네일로 추출 및 저장
        int frameNumber = 0;
        final String videoName = file.getOriginalFilename();
        final String thumbnailName = videoName.substring(0, videoName.length()-4) + "_thumbnail.png";
        final File video = s3Uploader.convert(file);
        final File thumbnail = new File(TEMP_FILE_PATH + thumbnailName);

        int thumbnail_width = 480;
        int thumbnail_height = 270;

        try {
            Picture picture = FrameGrab.getFrameFromFile(video, frameNumber);
            BufferedImage buffer_original_image = AWTUtil.toBufferedImage(picture);
            BufferedImage buffer_thumbnail_image = new BufferedImage(thumbnail_width, thumbnail_height, BufferedImage.TYPE_3BYTE_BGR);

            Graphics2D graphic = buffer_thumbnail_image.createGraphics();
            graphic.drawImage(buffer_original_image, 0, 0, thumbnail_width, thumbnail_height, null);

            ImageIO.write(buffer_thumbnail_image, "png", thumbnail);

            s3Uploader.removeNewFile(video);
            return s3Uploader.upload(thumbnail, dirName);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (JCodecException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
