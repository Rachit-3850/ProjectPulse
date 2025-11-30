package com.rachit.ProjectPulse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rachit.ProjectPulse.model.entity.Task;
import com.rachit.ProjectPulse.model.entity.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    Page<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);

    Page<Task> findByProjectIdAndTitleContainingIgnoreCase(Long projectId, String title, Pageable pageable);
}

