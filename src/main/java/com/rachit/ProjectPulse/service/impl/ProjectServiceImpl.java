package com.rachit.ProjectPulse.service.impl;

import com.rachit.ProjectPulse.exception.BadRequestException;
import com.rachit.ProjectPulse.exception.ResourceNotFoundException;
import com.rachit.ProjectPulse.exception.UnauthorizedException;
import com.rachit.ProjectPulse.model.dto.ProjectDto;
import com.rachit.ProjectPulse.model.dto.ProjectMapper;
import com.rachit.ProjectPulse.model.entity.Project;
import com.rachit.ProjectPulse.model.entity.User;
import com.rachit.ProjectPulse.repository.ProjectRepository;
import com.rachit.ProjectPulse.repository.UserRepository;
import com.rachit.ProjectPulse.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    // ---------------------------
    // CREATE PROJECT
    // ---------------------------
    @Override
    public ProjectDto createProject(String ownerEmail, ProjectDto dto) {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + ownerEmail));

        Project project = projectMapper.toEntity(dto);
        project.setOwner(owner);

        project.getMembers().add(owner); // owner always a member

        Project saved = projectRepository.save(project);

        return projectMapper.toDto(saved);
    }

    // ---------------------------
    // GET PROJECTS WITH PAGINATION + SORTING
    // ---------------------------
    @Override
    public Page<ProjectDto> getMyProjects(String ownerEmail, int page, int size,
                                          String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Project> projects =
                projectRepository.findByOwnerEmail(ownerEmail, pageable);

        return projects.map(projectMapper::toDto);
    }

    // ---------------------------
    // SEARCH PROJECTS
    // ---------------------------
    @Override
    public Page<ProjectDto> searchMyProjects(String ownerEmail, String keyword,
                                             int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Project> projects =
                projectRepository.findByOwnerEmailAndNameContainingIgnoreCase(
                        ownerEmail, keyword, pageable);

        return projects.map(projectMapper::toDto);
    }

    // ---------------------------
    // GET PROJECT BY ID (OWNER ONLY)
    // ---------------------------
    @Override
    public ProjectDto getMyProjectById(String ownerEmail, Long id) {
        Project project = getOwnedProject(ownerEmail, id);
        return projectMapper.toDto(project);
    }

    // ---------------------------
    // UPDATE PROJECT
    // ---------------------------
    @Override
    public ProjectDto updateProject(String ownerEmail, Long id, ProjectDto dto) {

        Project project = getOwnedProject(ownerEmail, id);

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BadRequestException("Project name cannot be empty");
        }

        if (dto.getName() != null) project.setName(dto.getName());
        if (dto.getDescription() != null) project.setDescription(dto.getDescription());
        if (dto.getStartDate() != null) project.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) project.setEndDate(dto.getEndDate());
        if (dto.getPriority() != null) project.setPriority(dto.getPriority());
        if (dto.getTags() != null) project.setTags(dto.getTags());

        Project saved = projectRepository.save(project);

        return projectMapper.toDto(saved);
    }

    // ---------------------------
    // DELETE PROJECT (OWNER ONLY)
    // ---------------------------
    @Override
    public void deleteProject(String ownerEmail, Long id) {
        Project project = getOwnedProject(ownerEmail, id);
        projectRepository.delete(project);
    }

    // ---------------------------
    // PRIVATE HELPER
    // ---------------------------
    private Project getOwnedProject(String ownerEmail, Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project not found with id: " + id));

        if (!project.getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedException("You are not allowed to access this project");
        }

        return project;
    }
}
