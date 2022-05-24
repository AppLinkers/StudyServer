package com.example.studyBridge_server.dto.assignedToDo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAssignedToDoRes {

    private Long id;

    private Long toDoId;

    private Long studyId;

    private Long menteeId;

    private String menteeName;

    private String menteeProfileImg;

    private String mentorName;

    private String mentorProfileImg;

    private String task;

    private String explain;

    private LocalDateTime dueDate;

    private String status;
}
