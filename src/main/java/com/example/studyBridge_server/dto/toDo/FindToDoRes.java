package com.example.studyBridge_server.dto.toDo;

import com.example.studyBridge_server.domaion.type.ToDoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindToDoRes {

    private Long id;

    private Long menteeId;

    private Long studyId;

    private String task;

    private LocalDateTime dueDate;

    private String feedBack;

    private ToDoStatus toDoStatus;
}
