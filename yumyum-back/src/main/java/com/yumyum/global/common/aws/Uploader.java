package com.yumyum.global.common.aws;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface Uploader {

    String upload(MultipartFile multipartFile, String dirName) throws IOException;
    String upload(File file, String dirName) throws IOException;
}
