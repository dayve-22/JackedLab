package com.dayve.userservice.controller;


import com.dayve.userservice.dto.RegisterRequest;
import com.dayve.userservice.dto.UserProfileDto;
import com.dayve.userservice.dto.UserResponse;
import com.dayve.userservice.model.User;
import com.dayve.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserDetails(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/{keycloakId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String keycloakId){
        return ResponseEntity.ok(userService.existByUserId(keycloakId));
    }

    @PostMapping
    public ResponseEntity<User> createUserProfile(
            @Valid @RequestBody UserProfileDto userProfileDto,
            @AuthenticationPrincipal Jwt jwt) {

        String keycloakId = jwt.getSubject();
        User savedUser = userService.createOrUpdateUserProfile(keycloakId, userProfileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    @PutMapping
    public ResponseEntity<User> updateUserProfile(
            @Valid @RequestBody UserProfileDto userProfileDto,
            @AuthenticationPrincipal Jwt jwt) {

        String keycloakId = jwt.getSubject();
        User savedUser = userService.createOrUpdateUserProfile(keycloakId, userProfileDto);
        return ResponseEntity.ok(savedUser);
    }


}
