package com.example.studyBridge_server.dto.feedBack;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindFeedBackRes {

    private Long id;

    private Long assignedToDoId;

    private Long writerId;

    private String writerName;

    private String writerProfileImg;

    private String comment;
}
