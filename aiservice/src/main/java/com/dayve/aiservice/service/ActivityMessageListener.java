package com.dayve.aiservice.service;


import com.dayve.aiservice.model.Activity;
import com.dayve.aiservice.model.Recommendation;
import com.dayve.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityMessageListener {
    private final ActivityAiService activityAiService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        log.info("Received activity for processing with id :{}",activity.getId());
        log.info("Generated recommendation: {}","Test");
        Recommendation recommendation = activityAiService.generateResponse(activity);
        recommendationRepository.save(recommendation);
    }
}
