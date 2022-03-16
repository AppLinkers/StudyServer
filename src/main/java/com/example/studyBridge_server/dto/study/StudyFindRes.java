package com.example.studyBridge_server.dto.study;

import com.example.studyBridge_server.domaion.type.StudyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyFindRes {

    private Long id;

    private String name;

    private String info;

    private String explain;

    private Integer maxNum;

    private String status;

    private String place;

    private String type;

    private int menteeCnt;
}
