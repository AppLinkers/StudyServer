package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.dto.study.StudyMakeReq;
import com.example.studyBridge_server.dto.study.StudyMakeRes;
import com.example.studyBridge_server.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @PostMapping("/make")
    public ResponseEntity<StudyMakeRes> make(@RequestBody StudyMakeReq studyMakeReq) {
        return ResponseEntity.status(201).body(studyService.make(studyMakeReq));
    }
}
