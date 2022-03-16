package com.example.studyBridge_server.dto.toDo;

import lombok.Data;

@Data
public class ChangeToDoStatusReq {

    private Long menteeId;

    private Long toDoId;

    private String status;
}
