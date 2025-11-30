package com.rachit.ProjectPulse.model.entity;
import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity                         // JPA entity -> maps to a table
@Table(name = "users",          // table name in DB
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_users_email", columnNames = "email")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id                                         // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;                    // stored ENCRYPTED (BCrypt)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;
    
    // A user can own/manage multiple projects
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Project> ownedProjects;
//
    // A user can be a member of many projects
    @ManyToMany(mappedBy = "members")
    private Set<Project> memberProjects;

}

