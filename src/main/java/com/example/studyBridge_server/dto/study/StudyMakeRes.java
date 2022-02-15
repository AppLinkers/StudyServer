package com.example.studyBridge_server.dto.study;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyMakeRes {

    private String name;

    private String makerId;
}
