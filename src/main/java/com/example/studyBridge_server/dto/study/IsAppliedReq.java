package com.example.studyBridge_server.dto.study;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsAppliedReq {

    private Long studyId;

    private String userLoginId;
}
