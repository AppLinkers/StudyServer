package com.example.studyBridge_server.dto.userMentor;

import com.example.studyBridge_server.domaion.Certificate;
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

    private List<Certificate> certificates;

    private String experience;

    private String curriculum;

    private String appeal;

}
