package com.dayve.userservice.dto;

import java.time.LocalDateTime;


public record UserResponse(
    String id,
    String firstName,
    String lastName,
    String email,
    String password,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)
{
}
