package com.example.studyBridge_server.dto.toDo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ChangeToDoStatusReq {

    private Long menteeId;

    private Long toDoId;

    private String status;
}
