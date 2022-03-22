package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.dto.userMentee.LikeMentorRes;
import com.example.studyBridge_server.service.UserMenteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/mentee")
@RequiredArgsConstructor
public class UserMenteeController {

    private final UserMenteeService userMenteeService;

    @PostMapping("/like")
    public ResponseEntity<LikeMentorRes> likeMentor(@RequestParam Long menteeId, @RequestParam Long mentorId) {
        return ResponseEntity.status(201).body(userMenteeService.likeMentor(menteeId, mentorId));
    }

    @PostMapping("/unlike")
    public ResponseEntity<LikeMentorRes> unLikeMentor(@RequestParam Long menteeId, @RequestParam Long mentorId) {
        return ResponseEntity.status(201).body(userMenteeService.unLikeMentor(menteeId, mentorId));
    }

    @GetMapping("/like")
    public ResponseEntity<List<LikeMentorRes>> findLikedMentors(@RequestParam Long menteeId) {
        return ResponseEntity.status(201).body(userMenteeService.findLikedMentors(menteeId));
    }
}
