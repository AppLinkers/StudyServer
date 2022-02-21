package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.dto.study.*;
import com.example.studyBridge_server.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @PostMapping("/make")
    public ResponseEntity<StudyMakeRes> make(@RequestBody StudyMakeReq studyMakeReq) {
        return ResponseEntity.status(201).body(studyService.make(studyMakeReq));
    }

    @PostMapping("/apply")
    public ResponseEntity<StudyApplyRes> apply(@RequestBody StudyApplyReq studyApplyReq) {
        return ResponseEntity.status(201).body(studyService.apply(studyApplyReq));
    }

    @GetMapping()
    public ResponseEntity<List<StudyFindRes>> find() {
        return ResponseEntity.status(201).body(studyService.find());
    }

    @GetMapping("/isApplied")
    public ResponseEntity<Boolean> isApplied(@RequestParam Long studyID, @RequestParam String userLoginId) {

        IsAppliedReq isAppliedReq = new IsAppliedReq();
        isAppliedReq.setStudyId(studyID);
        isAppliedReq.setUserLoginId(userLoginId);

        return ResponseEntity.status(201).body(studyService.isAppliedByUser(isAppliedReq));
    }
}
