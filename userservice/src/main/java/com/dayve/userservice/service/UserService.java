package com.dayve.userservice.service;


import com.dayve.userservice.dto.RegisterRequest;
import com.dayve.userservice.dto.UserResponse;
import com.dayve.userservice.model.User;
import com.dayve.userservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponse register(@Valid RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exist");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(),savedUser.getFirstName(),savedUser.getLastName(),
                savedUser.getEmail(),savedUser.getPassword(),savedUser.getCreatedAt(),savedUser.getUpdatedAt());
    }

    public UserResponse getUserDetails(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("No user found"));
        return new UserResponse(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public Boolean existByUserId(String userId) {
        return userRepository.existsById(userId);
    }
}
