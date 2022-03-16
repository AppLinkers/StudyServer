package com.example.studyBridge_server.dto.userMentor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileTextReq {

    private String userLoginId;

    private String location;

    private String info;

    private String nickName;

    private String subject;

    private String school;

    private Optional<List<String>> certificates;

    private String experience;

    private String curriculum;

    private String appeal;

}
