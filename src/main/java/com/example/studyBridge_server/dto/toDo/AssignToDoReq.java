package com.example.studyBridge_server.dto.toDo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignToDoReq {

    private Long studyId;

    private Long mentorId;

    private String task;

    private LocalDateTime dueDate;

}
