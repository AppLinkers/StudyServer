package com.example.studyBridge_server.dto.userAuth;

import com.example.studyBridge_server.domain.type.Gender;
import com.example.studyBridge_server.domain.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileRes {

    private String loginId;

    private String name;

    private String phone;

    private String profileImg;

    private String location;

    private Gender gender;

    private Role role;

}
