package com.example.studyBridge_server.dto.userAuth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignUpReq {

    private String name;

    private String loginId;

    private String loginPw;

    private String role; // Role.valueOf(role) 로 형 변환시켜 주기

    private String phone;

    private String gender; // Gender.valueOf(gender) 로 형 변환시켜주기

    private String location;

//    private MultipartFile profileImg; // img -> s3 -> url 값 저장
}
