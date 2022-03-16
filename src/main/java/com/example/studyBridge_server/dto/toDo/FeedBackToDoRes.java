package com.example.studyBridge_server.dto.toDo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackToDoRes {
    private Long mentorId;

    private Long toDoId;

    private String feedBack;
}
