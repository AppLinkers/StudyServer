package com.example.studyBridge_server.dto.assignedToDo;

import lombok.Data;

@Data
public class ConfirmToDoReq {

    private Long mentorId;

    private Long assignedToDoId;

}
