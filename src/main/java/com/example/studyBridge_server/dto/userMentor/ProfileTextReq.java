package com.example.studyBridge_server.dto.userMentor;

import lombok.Data;

@Data
public class ProfileTextReq {

    private String userLoginId;

    private String location;

    private String info;

    private String nickName;

    private String subject;

    private String school;

    private String experience;

    private String curriculum;

    private String appeal;

}
