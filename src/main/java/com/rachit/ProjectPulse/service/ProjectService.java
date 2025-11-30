package com.rachit.ProjectPulse.service;

import com.rachit.ProjectPulse.model.dto.ProjectDto;
import org.springframework.data.domain.Page;

public interface ProjectService {

    ProjectDto createProject(String ownerEmail, ProjectDto dto);

    Page<ProjectDto> getMyProjects(String ownerEmail, int page, int size, String sortBy, String sortOrder);

    Page<ProjectDto> searchMyProjects(String ownerEmail, String keyword, int page, int size);

    ProjectDto getMyProjectById(String ownerEmail, Long id);

    ProjectDto updateProject(String ownerEmail, Long id, ProjectDto dto);
    
    void addMember(String ownerEmail, Long projectId, Long userId);
    
    void removeMember(String ownerEmail, Long projectId, Long userId);

    void deleteProject(String ownerEmail, Long id);
}
