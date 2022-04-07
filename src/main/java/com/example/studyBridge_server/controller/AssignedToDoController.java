package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.domain.type.ToDoStatus;
import com.example.studyBridge_server.dto.assignedToDo.*;
import com.example.studyBridge_server.service.AssignedToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/toDo/assigned")
@RequiredArgsConstructor
public class AssignedToDoController {

    private final AssignedToDoService assignedToDoService;

    /**
     * AssignedToDo 상태 변경
     */
    @PostMapping("/status")
    public ResponseEntity<ChangeToDoStatusRes> changeStatus(@RequestBody ChangeToDoStatusReq changeToDoStatusReq) {
        try {
            return ResponseEntity.status(201).body(assignedToDoService.changeStatus(changeToDoStatusReq));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ChangeToDoStatusRes());
        }
    }

    /**
     * AssignedToDo 상태 CONFIRMED 로 변경
     */
    @PostMapping("/confirm")
    public ResponseEntity<ConfirmToDoRes> confirm(@RequestBody ConfirmToDoReq confirmToDoReq) {
        try {
            return ResponseEntity.status(201).body(assignedToDoService.confirm(confirmToDoReq));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ConfirmToDoRes());
        }
    }

    /**
     * 특정 멘티의 AssignedToDo 개수 반환
     */
    @GetMapping("/mentee/cnt")
    public ResponseEntity<Integer> countByMentee(@RequestParam("menteeId") Long menteeId) {
        return ResponseEntity.status(201).body(assignedToDoService.countByMentee(menteeId));
    }

    /**
     * 특정 멘티의 특정 상태인 AssignedToDo 개수 반환
     */
    @GetMapping("/mentee/status/cnt")
    public ResponseEntity<Integer> countByMenteeAndStatus(@RequestParam("menteeId") Long menteeId, @RequestParam("status") ToDoStatus status) {
        return ResponseEntity.status(201).body(assignedToDoService.countByMenteeAndStatus(menteeId, status));
    }

    /**
     * 특정 멘티의 특정 스터디의 List<AssignedToDo> 전체 개수 및 Confirmed 개수 반환
     */
    @GetMapping("/mentee/studyId/cnt")
    public ResponseEntity<Map<String, Integer>> countByMenteeAndStudy(@RequestParam("menteeId") Long menteeId, @RequestParam("studyId") Long studyId) {
        return ResponseEntity.status(201).body(assignedToDoService.countByMenteeAndStudy(menteeId, studyId));
    }

    /**
     * 특정 멘티의 특정 상태인 List<AssignedToDo> 반환
     */
    @GetMapping("/mentee/status")
    public ResponseEntity<List<FindAssignedToDoRes>> findByMenteeAndStatus(@RequestParam("menteeId") Long menteeId, @RequestParam("status") ToDoStatus status) {
        return ResponseEntity.status(201).body(assignedToDoService.findByMenteeAndStatus(menteeId, status));
    }

    /**
     * ToDo에서 배정된 List<AssignedToDo> 반환
     */
    @GetMapping("/{toDoId}")
    public ResponseEntity<List<FindAssignedToDoRes>> findByToDo(@PathVariable("toDoId") Long toDoId) {
        return ResponseEntity.status(201).body(assignedToDoService.findByToDo(toDoId));
    }

    /**
     * 특정 멘티의 List<AssignedToDo> 반환
     */
    @GetMapping("/mentee")
    public ResponseEntity<List<FindAssignedToDoRes>> findByMentee(@RequestParam("menteeId") Long menteeId) {
        return ResponseEntity.status(201).body(assignedToDoService.findByMentee(menteeId));
    }


}
