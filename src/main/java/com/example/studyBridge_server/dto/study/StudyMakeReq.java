package com.example.studyBridge_server.dto.study;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyMakeReq {

    private String makerId;

    private String name;

    private String type;

    private String info;

    private String explain;

    private String place;

    private Integer maxNum;
}
