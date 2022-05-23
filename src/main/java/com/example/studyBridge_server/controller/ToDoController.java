package com.example.studyBridge_server.controller;

import com.example.studyBridge_server.dto.toDo.*;
import com.example.studyBridge_server.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/toDo")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    /**
     * ToDo 할당
     */
    @PostMapping("/")
    public ResponseEntity<AssignToDoRes> assign(@RequestBody AssignToDoReq assignToDoReq) {
        try {
            return ResponseEntity.status(201).body(toDoService.assign(assignToDoReq));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new AssignToDoRes());
        }
    }

    /**
     * 특정 멘토가 소속된 스터디들의 List<ToDo> 반환
     */
    @GetMapping("/mentor")
    public ResponseEntity<List<FindToDoRes>> findOfMentor(@RequestParam("mentorId") Long mentorId) {
        return ResponseEntity.status(201).body(toDoService.findToDoByMentor(mentorId));
    }

    /**
     * 특정 스터디의 List<ToDo> 반환
     */
    @GetMapping("/study")
    public ResponseEntity<List<FindToDoRes>> findOfStudy(@RequestParam("studyId") Long studyId, @RequestParam("mentorId") Long mentorId) {
        try {
            return ResponseEntity.status(201).body(toDoService.findOfStudy(new FindToDoReq(mentorId, studyId)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }

    /**
     * 특정 ToDo 삭제 기능
     */
    @PostMapping("/delete")
    public ResponseEntity<Integer> delete(@RequestParam("toDoId") Long toDoId) {
        return ResponseEntity.status(201).body(toDoService.delete(toDoId));
    }

    @PostMapping("/update/dueDate")
    public ResponseEntity<Integer> updateDueDate(@RequestBody UpdateToDoDueDateReq request) {
        return ResponseEntity.status(201).body(toDoService.updateDueDate(request));
    }
}
