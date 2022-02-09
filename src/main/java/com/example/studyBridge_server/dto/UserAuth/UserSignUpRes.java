package com.example.studyBridge_server.dto.UserAuth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignUpRes {

    private String loginId;
    private String name;
}
