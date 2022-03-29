package com.example.studyBridge_server.dto.feedBack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WriteFeedBackRes {

    private Long id;

    private Long assignedToDoId;

    private Long writerId;

    private String comment;
}
