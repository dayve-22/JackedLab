package com.dayve.userservice.repository;

import com.dayve.userservice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(@NotBlank(message = "email is required") @Email(message = "invalid email") String email);

    User findByEmail(String email);

    Boolean existsByKeycloakId(String keycloakId);
}
