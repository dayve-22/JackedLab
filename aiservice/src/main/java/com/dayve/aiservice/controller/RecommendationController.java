package com.dayve.aiservice.controller;


import com.dayve.aiservice.model.Activity;
import com.dayve.aiservice.model.Recommendation;
import com.dayve.aiservice.service.ActivityAiService;
import com.dayve.aiservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final ActivityAiService activityAiService;


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendation(@PathVariable String userId){
        return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendation(@PathVariable String activityId){
        return ResponseEntity.ok(recommendationService.getActivityRecommendation(activityId));
    }

    @PostMapping("/activity/recommend")
    public ResponseEntity<Recommendation> getAiRecommendation(@RequestBody Activity activity){
        return ResponseEntity.ok(activityAiService.generateResponse(activity));
    }
}
