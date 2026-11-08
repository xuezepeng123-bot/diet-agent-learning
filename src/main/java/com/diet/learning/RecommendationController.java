package com.diet.learning;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/api/recommendations")
    public RecommendationService.Result recommend(@RequestBody Request request) {
        return recommendationService.recommend(request.message());
    }

    public record Request(String message) {}
}