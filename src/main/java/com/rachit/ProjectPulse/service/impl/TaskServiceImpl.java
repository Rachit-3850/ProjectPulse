
package com.rachit.ProjectPulse.service.impl;

import com.rachit.ProjectPulse.exception.BadRequestException;
import com.rachit.ProjectPulse.exception.ResourceNotFoundException;
import com.rachit.ProjectPulse.exception.UnauthorizedException;
import com.rachit.ProjectPulse.model.dto.TaskDto;
import com.rachit.ProjectPulse.model.dto.TaskMapper;
import com.rachit.ProjectPulse.model.entity.*;
import com.rachit.ProjectPulse.repository.*;
import com.rachit.ProjectPulse.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    // ---------------------------
    // CREATE TASK
    // ---------------------------
    @Override
    public TaskDto createTask(String email, TaskDto dto) {

        if (dto.getAssigneeId() == null) {
            throw new BadRequestException("Task must have an assignee");
        }

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Only project members can create tasks
        if (!project.getMembers().contains(creator)) {
            throw new UnauthorizedException("Only project members can create tasks");
        }

        User assignee = userRepository.findById(dto.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));

        // Assignee must be project member
        if (!project.getMembers().contains(assignee)) {
            throw new BadRequestException("Assignee must be a project member");
        }

        Task task = taskMapper.toEntity(dto);
        task.setProject(project);
        task.setAssignee(assignee);

        Task saved = taskRepository.save(task);

        return taskMapper.toDto(saved);
    }

    // ---------------------------
    // GET TASK BY ID
    // ---------------------------
    @Override
    public TaskDto getTaskById(String email, Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Only owner + assignee can see
        if (!task.getProject().getOwner().getId().equals(user.getId()) &&
                !task.getAssignee().getId().equals(user.getId())) {
            throw new UnauthorizedException("You cannot view this task");
        }

        return taskMapper.toDto(task);
    }

    // ---------------------------
    // UPDATE TASK
    // ---------------------------
    @Override
    public TaskDto updateTask(String email, Long taskId, TaskDto dto) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Only owner or assignee can update
        if (!task.getProject().getOwner().getId().equals(user.getId()) &&
            !task.getAssignee().getId().equals(user.getId())) {
            throw new UnauthorizedException("Only owner or assignee can update task");
        }

        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getStartDate() != null) task.setStartDate(dto.getStartDate());
        if (dto.getDueDate() != null) task.setDueDate(dto.getDueDate());
        if (dto.getStatus() != null) task.setStatus(dto.getStatus());
        if (dto.getPriority() != null) task.setPriority(dto.getPriority());

        // Change assignee?
        if (dto.getAssigneeId() != null) {
            User newAssignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));

            if (!task.getProject().getMembers().contains(newAssignee)) {
                throw new BadRequestException("New assignee must be a project member");
            }

            task.setAssignee(newAssignee);
        }

        Task saved = taskRepository.save(task);

        return taskMapper.toDto(saved);
    }

    // ---------------------------
    // DELETE TASK
    // ---------------------------
    @Override
    public void deleteTask(String email, Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Only owner or assignee can delete
        if (!task.getProject().getOwner().getId().equals(user.getId()) &&
            !task.getAssignee().getId().equals(user.getId())) {
            throw new UnauthorizedException("Only owner or assignee can delete task");
        }

        taskRepository.delete(task);
    }

    // ---------------------------
    // GET TASKS BY PROJECT (PAGINATION)
    // ---------------------------
    @Override
    public Page<TaskDto> getTasksByProject(String email, Long projectId,
                                           int page, int size) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Only project members can view
        if (!project.getMembers().contains(user)) {
            throw new UnauthorizedException("You cannot view tasks of this project");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Task> tasks = taskRepository.findByProjectId(projectId, pageable);

        return tasks.map(taskMapper::toDto);
    }
}
