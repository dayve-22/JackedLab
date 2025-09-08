package com.dayve.userservice.model;

import com.dayve.userservice.model.enums.Sex;
import com.dayve.userservice.model.enums.UserRole;
import com.dayve.userservice.model.enums.WorkoutGoal;
import com.dayve.userservice.model.enums.WorkoutIntensity;
import jakarta.persistence.*;
import jakarta.ws.rs.Encoded;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;



@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String keycloakId;

    private String firstName;

    private String lastName;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    @Encoded
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    private Double weight;

    private Double height;

    private Integer age;

    private Sex sex;

    private WorkoutIntensity workoutIntensity;

    private WorkoutGoal workoutGoal;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
