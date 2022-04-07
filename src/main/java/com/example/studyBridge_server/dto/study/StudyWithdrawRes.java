package com.example.studyBridge_server.dto.study;

import com.example.studyBridge_server.domain.type.StudyStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyWithdrawRes {

    private Long studyId;

    private Long userId;

    private StudyStatus status;

}
