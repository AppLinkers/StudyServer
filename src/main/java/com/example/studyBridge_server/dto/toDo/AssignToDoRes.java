package com.example.studyBridge_server.dto.toDo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignToDoRes {

    private Long studyId;

    private Integer menteeCnt;

}
