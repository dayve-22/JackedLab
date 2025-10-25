package com.dayve.activityservice.controller;


import com.dayve.activityservice.dto.ActivityRequest;
import com.dayve.activityservice.dto.ActivityResponse;
import com.dayve.activityservice.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request,@RequestHeader("X-User-ID") String userId){
        if(userId!=null){
            request.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.trackActivity(request));
    }
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getAcitivity(@PathVariable String activityId){
        return ResponseEntity.ok(activityService.getAcitivityById(activityId));
    }
}
