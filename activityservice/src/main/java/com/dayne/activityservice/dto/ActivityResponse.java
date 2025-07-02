package com.dayne.activityservice.dto;

import com.dayne.activityservice.model.ActivityType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

public record ActivityResponse(String id,
                               String userId,
                               ActivityType type,
                               Integer duration,
                               Integer caloriesBurned,
                               LocalDateTime startTime,
                               Map<String,Object> additionalMetrics,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt ) {
}
