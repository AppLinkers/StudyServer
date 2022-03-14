package com.example.studyBridge_server.dto.userMentee;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeMentorRes {

    private Long menteeId;

    private Long mentorId;
}
