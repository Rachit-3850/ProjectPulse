package com.rachit.ProjectPulse.service;

import org.springframework.data.domain.Page;

import com.rachit.ProjectPulse.model.dto.TaskDto;

public interface TaskService {

    TaskDto createTask(String ownerEmail, TaskDto dto);
    TaskDto updateTask(String ownerEmail, Long taskId, TaskDto dto);
    void deleteTask(String ownerEmail, Long taskId);

    TaskDto getTaskById(String ownerEmail, Long taskId);

    Page<TaskDto> getTasksByProject(String ownerEmail, Long projectId, int page, int size);
}
