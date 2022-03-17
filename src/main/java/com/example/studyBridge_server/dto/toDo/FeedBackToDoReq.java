package com.example.studyBridge_server.dto.toDo;

import lombok.Data;

@Data
public class FeedBackToDoReq {

    private Long mentorId;

    private Long toDoId;

    private String feedBack;
}
