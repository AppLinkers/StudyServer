package com.example.studyBridge_server.dto.userMentor;

import com.example.studyBridge_server.domain.Certificate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRes {

    private Long userId;

    private String userLoginId;

    private String userName;

    private String profileImg;

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

    private Boolean liked;

}
