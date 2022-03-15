package com.example.studyBridge_server.dto.study;

import lombok.Builder;
import lombok.Data;

@Builder
public class StudyDeleteRes {

    private Long studyId;

    private Long makerId;
}
