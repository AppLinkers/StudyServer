package com.example.studyBridge_server.dto.userAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRes {

    private Long id;

    private String loginId;

    private String name;
}
