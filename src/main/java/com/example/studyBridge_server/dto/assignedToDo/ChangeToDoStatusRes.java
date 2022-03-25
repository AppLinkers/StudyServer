package com.example.studyBridge_server.dto.assignedToDo;

import com.example.studyBridge_server.domaion.type.ToDoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeToDoStatusRes {

    private Long menteeId;

    private Long assignedToDoId;

    private ToDoStatus status;
}
