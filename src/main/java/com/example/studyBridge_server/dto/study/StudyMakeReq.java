package com.example.studyBridge_server.dto.study;

import lombok.Data;

@Data
public class StudyMakeReq {

    private String makerId;

    private String name;

    private String type;

    private String info;

    private String explain;

    private String place;

    private Integer maxNum;
}
