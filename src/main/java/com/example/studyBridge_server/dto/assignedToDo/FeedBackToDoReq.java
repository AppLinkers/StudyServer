package com.example.studyBridge_server.dto.assignedToDo;

import lombok.Data;

@Data
public class FeedBackToDoReq {

    private Long mentorId;

    private Long assignedToDoId;

    private String feedBack;
}
