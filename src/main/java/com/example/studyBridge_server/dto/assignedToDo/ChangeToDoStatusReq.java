package com.example.studyBridge_server.dto.assignedToDo;

import lombok.Data;

@Data
public class ChangeToDoStatusReq {

    private Long menteeId;

    private Long assignedToDoId;

    private String status;
}
