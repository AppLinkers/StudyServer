package com.example.studyBridge_server.dto.study;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyMakeReq {

    private String makerId;

    private String name;

    private String type;

    private String info;

    private String place;

    private Integer maxNum;
}
