package com.example.studyBridge_server.dto.toDo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ConfirmToDoReq {

    private Long mentorId;

    private Long toDoId;

}
