package com.example.studyBridge_server.dto.study;

import lombok.Data;

@Data
public class StudyDeleteReq {

    private Long studyId;

    private Long makerId;
}
