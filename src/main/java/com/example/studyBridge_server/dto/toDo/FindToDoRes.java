package com.example.studyBridge_server.dto.toDo;

import com.example.studyBridge_server.domaion.type.ToDoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class FindToDoRes {

    private Long id;

    private Long menteeId;

    private Long studyId;

    private String task;

    private LocalDateTime dueDate;

    private String feedBack;

    private ToDoStatus toDoStatus;
}
