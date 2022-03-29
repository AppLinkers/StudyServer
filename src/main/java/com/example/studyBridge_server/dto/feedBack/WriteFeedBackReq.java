package com.example.studyBridge_server.dto.feedBack;

import lombok.Data;

@Data
public class WriteFeedBackReq {

    private Long assignedToDoId;

    private Long writerId;

    private String comment;
}
