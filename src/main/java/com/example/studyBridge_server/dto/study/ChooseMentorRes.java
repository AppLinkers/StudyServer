package com.example.studyBridge_server.dto.study;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChooseMentorRes {

    private Long studyId;

    private Long mentorId;

    private String studyName;

}