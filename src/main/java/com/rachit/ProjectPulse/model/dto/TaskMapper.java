package com.rachit.ProjectPulse.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rachit.ProjectPulse.model.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    TaskDto toDto(Task task);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    Task toEntity(TaskDto dto);
}
