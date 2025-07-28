package com.dayve.userservice.controller;


import com.dayve.userservice.dto.RegisterRequest;
import com.dayve.userservice.dto.UserResponse;
import com.dayve.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
