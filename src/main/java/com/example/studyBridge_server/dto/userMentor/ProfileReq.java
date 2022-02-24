package com.example.studyBridge_server.dto.userMentor;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProfileReq {

    private String userLoginId;

    private String location;

    private String info;

    private String nickName;

    private String subject;

    private String school;

    private MultipartFile schoolImg;

    private List<MultipartFile> certificatesImg;

    private String experience;

    private String curriculum;

    private String appeal;

}
