package com.example.studyBridge_server.dto.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindRoomRes {

    private Long roomId;

    private Long studyId;
}
