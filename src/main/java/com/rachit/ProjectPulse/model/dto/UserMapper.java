package com.rachit.ProjectPulse.model.dto;

import com.rachit.ProjectPulse.model.entity.User;
import java.util.*;
import com.rachit.ProjectPulse.model.entity.UserRole;

public class UserMapper {

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(UserRole.USER);  // default role
        // password will be set in service
        user.setOwnedProjects(new ArrayList<>());
        user.setMemberProjects(new HashSet<>());
        return user;
    }

    public static UserDto toDto(User entity) {
        if (entity == null) return null;

        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }
}
