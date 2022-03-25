package com.example.studyBridge_server.dto.assignedToDo;

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

    private Long assignedToDoId;

    private String feedBack;
}
