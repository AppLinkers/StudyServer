package com.example.studyBridge_server.dto.userMentor;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProfileRes {

    private String userName;

    private String location;

    private String info;

    private String nickName;

    private String school;

    private String schoolImg;

    private String subject;

    private List<String> certificatesImg;

    private String curriculum;

    private String appeal;

}
