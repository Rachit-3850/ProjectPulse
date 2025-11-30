package com.rachit.ProjectPulse.repository;

import com.rachit.ProjectPulse.model.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByOwnerEmail(String email, Pageable pageable);

    Page<Project> findByOwnerEmailAndNameContainingIgnoreCase(
            String email,
            String name,
            Pageable pageable
    );
}
