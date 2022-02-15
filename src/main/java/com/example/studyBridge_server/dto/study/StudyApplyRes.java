package com.example.studyBridge_server.dto.study;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyApplyRes {

    private String studyName;

    private String userName;

    private String userType;
}
