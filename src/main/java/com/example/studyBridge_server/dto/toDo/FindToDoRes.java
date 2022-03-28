package com.example.studyBridge_server.dto.toDo;

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

    private Long studyId;

    private String task;

    private String explain;

    private LocalDateTime dueDate;

}
