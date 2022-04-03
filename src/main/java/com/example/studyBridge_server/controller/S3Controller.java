package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Uploader s3Uploader;

    @PostMapping("/")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }

    @PostMapping("/chat/one")
    public ResponseEntity<String> chatImg(@ModelAttribute("image") MultipartFile image) throws IOException {
        if (image == null) {
            return ResponseEntity.badRequest().body("image file null");
        } else {
            return ResponseEntity.status(201).body(s3Uploader.upload(image, "chat"));
        }
    }
}
