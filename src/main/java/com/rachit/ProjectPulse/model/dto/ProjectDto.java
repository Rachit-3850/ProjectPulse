package com.rachit.ProjectPulse.model.dto;

import com.rachit.ProjectPulse.model.entity.ProjectPriority;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {

    private Long id;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private ProjectPriority priority;

    private Set<String> tags;

    private Long ownerId;         // logged-in user

    private Set<Long> memberIds;  // users added to project
}
