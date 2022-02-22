package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.dto.userMentor.ProfileReq;
import com.example.studyBridge_server.dto.userMentor.ProfileRes;
import com.example.studyBridge_server.service.UserMentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("user/mentor")
@RequiredArgsConstructor
public class UserMentorController {

    private final UserMentorService userMentorService;

    @PostMapping("/profile")
    public ResponseEntity<Object> profile(@ModelAttribute ProfileReq profileReq) {
        try {
            return ResponseEntity.status(201).body(userMentorService.profile(profileReq));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
