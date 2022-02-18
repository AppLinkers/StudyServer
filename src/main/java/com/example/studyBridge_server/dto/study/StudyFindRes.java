package com.example.studyBridge_server.dto.study;

import com.example.studyBridge_server.domaion.type.StudyStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyFindRes {

    private Long id;

    private String name;

    private String info;

    private Integer maxNum;

    private String status;

    private String place;

    private String type;
}
