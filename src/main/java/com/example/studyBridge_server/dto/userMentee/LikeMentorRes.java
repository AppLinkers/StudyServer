package com.example.studyBridge_server.dto.userMentee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class LikeMentorRes {

    private Long menteeId;

    private Long mentorId;
}
