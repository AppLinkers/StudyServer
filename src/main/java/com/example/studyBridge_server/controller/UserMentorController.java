package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.dto.userMentor.ProfileReq;
import com.example.studyBridge_server.dto.userMentor.ProfileRes;
import com.example.studyBridge_server.service.UserMentorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/mentor")
@RequiredArgsConstructor
public class UserMentorController {

    private final UserMentorService userMentorService;

    @SneakyThrows
    @PostMapping("/profile")
    public ResponseEntity<ProfileRes> profile(@ModelAttribute ProfileReq profileReq) {
        return ResponseEntity.status(201).body(userMentorService.profile(profileReq));
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileRes> getProfile(@RequestParam String mentorLoginId, @RequestParam String userLoginId) {
        return ResponseEntity.status(201).body(userMentorService.getProfile(mentorLoginId, userLoginId));
    }

    @GetMapping("/allProfile")
    public ResponseEntity<List<ProfileRes>> getAllProfile(@RequestParam String userLoginId) {
        return ResponseEntity.status(201).body(userMentorService.getAllProfile(userLoginId));
    }
}
