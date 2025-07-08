package com.dayne.activityservice.service;


import com.dayne.activityservice.dto.ActivityRequest;
import com.dayne.activityservice.dto.ActivityResponse;
import com.dayne.activityservice.model.Activity;
import com.dayne.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;

    public ActivityResponse trackActivity(ActivityRequest request) {
        if(!userValidationService.validateUser(request.getUserId())){
            throw new RuntimeException("Invalid User: " + request.getUserId());
        }

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

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities= activityRepository.findByUserId(userId);
        return activities.stream().map(activity -> new ActivityResponse(activity.getId(), activity.getUserId(),activity.getType(),activity.getDuration()
        ,activity.getCaloriesBurned(),activity.getStartTime(),activity.getAdditionalMetrics(),activity.getCreatedAt(),activity.getUpdatedAt()) ).toList();
    }

    public ActivityResponse getAcitivityById(String activityId) {
        return activityRepository.findById(activityId).map(activity -> new ActivityResponse(activity.getId(), activity.getUserId(),activity.getType(),activity.getDuration()
                ,activity.getCaloriesBurned(),activity.getStartTime(),activity.getAdditionalMetrics(),activity.getCreatedAt(),activity.getUpdatedAt()))
                .orElseThrow(()->new RuntimeException("Activity not found with this id"));
    }
}
