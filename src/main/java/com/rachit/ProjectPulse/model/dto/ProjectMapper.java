package com.rachit.ProjectPulse.model.dto;

import com.rachit.ProjectPulse.model.entity.Project;
import com.rachit.ProjectPulse.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(
            target = "memberIds",
            expression = "java(toMemberIdSet(project.getMembers()))"
    )
    ProjectDto toDto(Project project);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "members", expression = "java(new java.util.HashSet<>())")  // FIXED
    Project toEntity(ProjectDto dto);

    List<ProjectDto> toDtoList(List<Project> projects);

    default Set<Long> toMemberIdSet(Set<User> members) {
        if (members == null) return null;
        return members.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }
}

