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
public class ConfirmToDoRes {

    private Long mentorId;

    private Long assignedToDoId;

    private ToDoStatus toDoStatus;
}
