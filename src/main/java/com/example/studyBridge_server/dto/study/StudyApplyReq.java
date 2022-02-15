package com.example.studyBridge_server.dto.study;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyApplyReq {

    private String userId;

    private Long studyId;
}
