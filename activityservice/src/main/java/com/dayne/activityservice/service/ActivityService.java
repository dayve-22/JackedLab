package com.dayne.activityservice.service;


import com.dayne.activityservice.dto.ActivityRequest;
import com.dayne.activityservice.dto.ActivityResponse;
import com.dayne.activityservice.model.Activity;
import com.dayne.activityservice.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityResponse trackActivity(ActivityRequest request) {
        Activity activity = Activity.builder().userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
        Activity savedActivity = activityRepository.save(activity);
        return new ActivityResponse(savedActivity.getId(),
                savedActivity.getUserId(),
                savedActivity.getType(),
                savedActivity.getDuration(),
                savedActivity.getCaloriesBurned(),
                savedActivity.getStartTime(),
                savedActivity.getAdditionalMetrics(),
                savedActivity.getCreatedAt(),
                savedActivity.getUpdatedAt());
    }

}
