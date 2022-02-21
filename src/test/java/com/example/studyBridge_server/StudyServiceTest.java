package com.example.studyBridge_server;

import com.example.studyBridge_server.dto.study.IsAppliedReq;
import com.example.studyBridge_server.service.StudyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudyServiceTest {

    @Autowired
    private StudyService studyService;

    @Test
    void isAppliedByUser() {

        IsAppliedReq isAppliedReq = new IsAppliedReq();
        isAppliedReq.setStudyId(1L);
        isAppliedReq.setUserLoginId("test_loginId");

        Boolean result = studyService.isAppliedByUser(isAppliedReq);

        Assertions.assertThat(result);

    }
}