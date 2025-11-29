package com.rachit.ProjectPulse.model.dto;

import com.rachit.ProjectPulse.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "user.id", target = "userId")
    TaskDto toDto(Task task);

    @Mapping(target = "user", ignore = true)
    Task toEntity(TaskDto dto);

    List<TaskDto> toDtoList(List<Task> tasks);
}
