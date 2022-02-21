package com.example.studyBridge_server.dto.study;

import lombok.Data;

@Data
public class IsAppliedReq {

    private Long studyId;

    private String userLoginId;
}
