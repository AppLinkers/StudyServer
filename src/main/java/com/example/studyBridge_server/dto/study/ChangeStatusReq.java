package com.example.studyBridge_server.dto.study;

import lombok.Data;

@Data
public class ChangeStatusReq {

    private Long studyId;

    private String status;
}
