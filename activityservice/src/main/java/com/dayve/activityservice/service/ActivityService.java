package com.dayve.activityservice.service;


import com.dayve.activityservice.dto.ActivityRequest;
import com.dayve.activityservice.dto.ActivityResponse;
import com.dayve.activityservice.model.Activity;
import com.dayve.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

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
        try{
            rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
        } catch (Exception e) {
            log.error("Failed to publish activity to queue");
        }
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
