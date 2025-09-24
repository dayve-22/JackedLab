package com.dayve.userservice.service;


import com.dayve.userservice.dto.RegisterRequest;
import com.dayve.userservice.dto.UserProfileDto;
import com.dayve.userservice.dto.UserResponse;
import com.dayve.userservice.model.User;
import com.dayve.userservice.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponse register(@Valid RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            User existingUser = userRepository.findByEmail(request.getEmail());
            return new UserResponse(existingUser.getId(), existingUser.getKeycloakId(), existingUser.getFirstName(), existingUser.getLastName(),
                    existingUser.getEmail(), existingUser.getPassword(), existingUser.getCreatedAt(), existingUser.getUpdatedAt());
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setKeycloakId(request.getKeycloakId());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(),savedUser.getKeycloakId(),savedUser.getFirstName(),savedUser.getLastName(),
                savedUser.getEmail(),savedUser.getPassword(),savedUser.getCreatedAt(),savedUser.getUpdatedAt());
    }

    public UserResponse getUserDetails(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("No user found"));
        return new UserResponse(user.getId(),
                user.getKeycloakId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public Boolean existByUserId(String keycloakId) {
        log.info("Calling User validation api for keycloakId: {}",keycloakId);
        return userRepository.existsByKeycloakId(keycloakId);
    }

    public User createOrUpdateUserProfile(String keycloakId, @Valid UserProfileDto userProfileDto) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElse(new User());

        if (user.getId() == null) {
            user.setKeycloakId(keycloakId);
        }
        user.setHeight(userProfileDto.height());
        user.setWeight(userProfileDto.weight());
        user.setAge(userProfileDto.age());
        user.setSex(userProfileDto.sex());
        user.setWorkoutGoal(userProfileDto.workoutGoal());
        user.setWorkoutIntensity(userProfileDto.workoutIntensity());
        return userRepository.save(user);
    }
}
