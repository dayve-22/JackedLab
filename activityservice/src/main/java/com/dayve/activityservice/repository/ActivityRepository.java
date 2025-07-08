package com.dayne.activityservice.repository;

import com.dayne.activityservice.dto.ActivityResponse;
import com.dayne.activityservice.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository extends MongoRepository<Activity,String> {
    List<Activity> findByUserId(String userId);
}
