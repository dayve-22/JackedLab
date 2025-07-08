package com.dayve.aiservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "recommendations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {
    private String id;
    private String activityId;
    private String userId;
    private String activityType;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;
    @CreatedDate
    private LocalDateTime createdAt;

}
