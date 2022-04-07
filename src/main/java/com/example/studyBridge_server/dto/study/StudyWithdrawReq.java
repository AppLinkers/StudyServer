package com.example.studyBridge_server.dto.study;

import lombok.Data;

@Data
public class StudyWithdrawReq {

    private Long studyId;

    private Long userId;
}
