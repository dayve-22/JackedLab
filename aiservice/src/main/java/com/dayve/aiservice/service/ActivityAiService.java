package com.dayve.aiservice.service;


import com.dayve.aiservice.model.Activity;
import com.dayve.aiservice.model.Recommendation;
import com.dayve.aiservice.repository.RecommendationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {
    private final GeminiService geminiService;
    private final RecommendationRepository recommendationRepository;

    public Recommendation generateResponse(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getRecommendationFromAi(prompt);
        return parseAiResponse(activity,aiResponse);

    }

    private Recommendation parseAiResponse(Activity activity,String aiResponse){
        try {
            log.info("Raw AI Response: [{}]", aiResponse);
            int startIndex = aiResponse.indexOf('{');
            int endIndex = aiResponse.lastIndexOf('}');

            if (startIndex == -1 || endIndex == -1 || endIndex < startIndex) {
                log.error("Could not find a valid JSON object in the AI response.");
                return null;
            }

            String cleanJson = aiResponse.substring(startIndex, endIndex + 1);
            log.info("Cleaned JSON for Parsing: [{}]", cleanJson);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(cleanJson);
            JsonNode analysisNode = rootNode.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis,analysisNode,"overall","Overall:");
            addAnalysisSection(fullAnalysis,analysisNode,"calories_remark","Calories Remark:");

            List<String> improvementsList = new ArrayList<>();
            JsonNode improvementsNode = rootNode.path("improvements");
            if (improvementsNode.isArray()) {
                for (JsonNode improvement : improvementsNode) {
                    String area = improvement.path("area").asText("Unknown Area");
                    String recommendationText = improvement.path("recommendation").asText();
                    improvementsList.add(String.format("Area: %s - Recommendation: %s", area, recommendationText));
                }
            }

            List<String> suggestionsList = new ArrayList<>();
            JsonNode suggestionsNode = rootNode.path("suggestions");
            if (suggestionsNode.isArray()) {
                for (JsonNode suggestion : suggestionsNode) {
                    String workout = suggestion.path("workout").asText("Unknown Workout");
                    String description = suggestion.path("description").asText();
                    suggestionsList.add(String.format("Workout: %s - %s", workout, description));
                }
            }
            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .analysis(fullAnalysis.toString())
                    .improvements(improvementsList)
                    .suggestions(suggestionsList)
                    .createdAt(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.error("Failed to parse AI response: {}", aiResponse, e);
            return null;
        }
    }

    private void addAnalysisSection(StringBuilder fullAnalysis,JsonNode analysisNode,String key, String prefix ){
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix).append(analysisNode.path(key).asText()).append("\n\n");
        }
    }

    public String createPromptForActivity(Activity activity){
        log.info(activity.toString());
        return String.format("""
                Analyze the activity:
                Activity Type: %s
                Duration: %d minutes
                Calories Burned: %d
                Additional Metrics: %s
                Analyse the provided fitness activity and provide the details recommendation in the following EXACT JSON format:
                {
                 "analysis":{
                    "overall":"Overall analysis here",
                    "calories_remark": "Is it a good exercise for burning calories, Remarks here",
                 },
                 "improvements":[{
                   "area": "Area name",
                   "recommendation": "Detailed workout recommendation"
                  }
                ],
                {
                 "suggestions" : [
                   {
                    "workout": "Workout name",
                    "description": "Workout description"
                   }
                 ]
                }
                
                Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
                Ensure the response follows the EXACT JSON format shown above.
              
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics());
    }

}
