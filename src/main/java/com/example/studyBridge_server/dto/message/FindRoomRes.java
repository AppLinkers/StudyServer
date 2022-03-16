package com.example.studyBridge_server.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindRoomRes {

    private Long roomId;

    private Long studyId;
}
