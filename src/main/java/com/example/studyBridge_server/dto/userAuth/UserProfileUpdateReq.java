package com.example.studyBridge_server.dto.userAuth;


import com.example.studyBridge_server.domain.type.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileUpdateReq {

    private String loginId;

    private String name;

    private String phone;

    private Optional<MultipartFile> profileImg;

    private String location;

    private Gender gender;

}
