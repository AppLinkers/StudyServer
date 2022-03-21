package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.dto.toDo.*;
import com.example.studyBridge_server.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/toDo")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    @PostMapping("/")
    ResponseEntity<AssignToDoRes> assign(@RequestBody AssignToDoReq assignToDoReq) {
        try {
             return ResponseEntity.status(201).body(toDoService.assign(assignToDoReq));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AssignToDoRes());
        }
    }

    @PostMapping("/status")
    ResponseEntity<ChangeToDoStatusRes> changeStatus(@RequestBody ChangeToDoStatusReq changeToDoStatusReq) {
        try {
            return ResponseEntity.status(201).body(toDoService.changeStatus(changeToDoStatusReq));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ChangeToDoStatusRes());
        }
    }

    @PostMapping("/feedBack")
    ResponseEntity<FeedBackToDoRes> feedBack(@RequestBody FeedBackToDoReq feedBackToDoReq) {
        try {
            return ResponseEntity.status(201).body(toDoService.feedBack(feedBackToDoReq));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new FeedBackToDoRes());
        }
    }

    @PostMapping("/confirm")
    ResponseEntity<ConfirmToDoRes> confirm(@RequestBody ConfirmToDoReq confirmToDoReq) {
        try {
            return ResponseEntity.status(201).body(toDoService.confirm(confirmToDoReq));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ConfirmToDoRes());
        }
    }

    @GetMapping("/study")
    ResponseEntity<List<FindToDoRes>> findOfStudy(@RequestParam("studyId") Long studyId, @RequestParam("mentorId") Long mentorId) {
        try {
            return ResponseEntity.status(201).body(toDoService.findOfStudy(new FindToDoReq(studyId, mentorId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }

    @GetMapping("/mentee")
    ResponseEntity<Integer> countOfMentee(@RequestParam("menteeId") Long menteeId) {
        return ResponseEntity.status(201).body(toDoService.countOfMentee(menteeId));
    }

    @GetMapping("/mentee/confirmed")
    ResponseEntity<List<FindToDoRes>> findConfirmedOfMentee(@RequestParam("menteeId") Long menteeId) {
        return ResponseEntity.status(201).body(toDoService.findConfirmedOfMentee(menteeId));
    }
}
