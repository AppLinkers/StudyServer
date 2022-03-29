package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.dto.feedBack.FindFeedBackRes;
import com.example.studyBridge_server.dto.feedBack.WriteFeedBackReq;
import com.example.studyBridge_server.dto.feedBack.WriteFeedBackRes;
import com.example.studyBridge_server.service.FeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedBack")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @PostMapping("/")
    public ResponseEntity<WriteFeedBackRes> write(@RequestBody WriteFeedBackReq writeFeedBackReq) {
        return ResponseEntity.status(201).body(feedBackService.write(writeFeedBackReq));
    }

    @GetMapping("/assignedToDo")
    public ResponseEntity<List<FindFeedBackRes>> findByAssignedToDo(@RequestParam Long assignedToDoId) {
        return ResponseEntity.status(201).body(feedBackService.findByAssignedToDo(assignedToDoId));
    }

    @PostMapping("/delete")
    public ResponseEntity<Integer> delete(@RequestParam Long feedBackId) {
        return ResponseEntity.status(201).body(feedBackService.deleteById(feedBackId));
    }
}
