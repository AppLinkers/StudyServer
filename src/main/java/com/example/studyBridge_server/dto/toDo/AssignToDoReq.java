package com.example.studyBridge_server.dto.toDo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignToDoReq {

    private Long studyId;

    private Long mentorId;

    private String task;

    private LocalDateTime dueDate;

}
