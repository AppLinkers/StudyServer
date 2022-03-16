package com.example.studyBridge_server.dto.toDo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackToDoReq {

    private Long mentorId;

    private Long toDoId;

    private String feedBack;
}
