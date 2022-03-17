package com.example.studyBridge_server.dto.study;

import lombok.Data;

@Data
public class StudyUpdateReq {

    private Long studyId;

    private Long makerId;

    private String name;

    private String type;

    private String info;

    private String explain;

    private Integer maxNum;
}
