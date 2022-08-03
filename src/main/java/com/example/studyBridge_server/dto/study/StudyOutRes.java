package com.example.studyBridge_server.dto.study;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyOutRes {

    private Long studyId;

    private Long menteeId;
}
