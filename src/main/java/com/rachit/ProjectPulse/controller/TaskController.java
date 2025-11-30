package com.rachit.ProjectPulse.controller;

import com.rachit.ProjectPulse.model.dto.TaskDto;
import com.rachit.ProjectPulse.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @RequestAttribute("email") String email,
            @RequestBody TaskDto dto) {

        return ResponseEntity.ok(taskService.createTask(email, dto));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(
            @RequestAttribute("email") String email,
            @PathVariable Long taskId) {

        return ResponseEntity.ok(taskService.getTaskById(email, taskId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @RequestAttribute("email") String email,
            @PathVariable Long taskId,
            @RequestBody TaskDto dto) {

        return ResponseEntity.ok(taskService.updateTask(email, taskId, dto));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(
            @RequestAttribute("email") String email,
            @PathVariable Long taskId) {

        taskService.deleteTask(email, taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<Page<TaskDto>> getProjectTasks(
            @RequestAttribute("email") String email,
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(taskService.getTasksByProject(email, projectId, page, size));
    }
}
