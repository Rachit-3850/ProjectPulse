package com.rachit.ProjectPulse.model.dto;

import com.rachit.ProjectPulse.model.entity.Priority;
import com.rachit.ProjectPulse.model.entity.TaskStatus;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    private Long id;

    private String title;

    private String description;
    
    private LocalDate startDate;

    private LocalDate dueDate;

    private TaskStatus status;

    private Priority priority;

    private Long userId;
}
