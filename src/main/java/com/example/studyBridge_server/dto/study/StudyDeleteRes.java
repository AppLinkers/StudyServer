package com.example.studyBridge_server.dto.study;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyDeleteRes {

    private Long studyId;

    private Long makerId;

}
