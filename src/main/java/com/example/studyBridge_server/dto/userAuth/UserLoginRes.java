package com.example.studyBridge_server.dto.userAuth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginRes {

    private String loginId;

    private String name;
}