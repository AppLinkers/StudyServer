package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    @Autowired
    private final S3Uploader s3Uploader;

    @PostMapping("/s3")
    public String upload(@RequestParam("images")MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }

}
