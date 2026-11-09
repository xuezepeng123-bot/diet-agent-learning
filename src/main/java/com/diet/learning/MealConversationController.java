package com.diet.learning;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MealConversationController {
    private final ConcurrentMap<UUID, MealPreferenceExtractor.Preferences> sessions =
            new ConcurrentHashMap<>();

    private final MealPreferenceExtractor preferenceExtractor;
    private final RecommendationService recommendationService;

    public MealConversationController(MealPreferenceExtractor preferenceExtractor,
            RecommendationService recommendationService) {
        this.preferenceExtractor = preferenceExtractor;
        this.recommendationService = recommendationService;
    }

    @PostMapping("/api/conversations/recommendations")
    public Turn recommend(@RequestBody Request request) {
        if (request.message() == null || request.message().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请输入饮食需求");
        }

        UUID sessionId = request.sessionId();
        if (sessionId == null) {
            sessionId = UUID.randomUUID();
        } else if (!sessions.containsKey(sessionId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "会话不存在");
        }

        var latest = preferenceExtractor.extract(request.message());
        var merged = sessions.compute(sessionId, (id, previous) -> {
            if (previous == null) {
                return latest;
            }
            return new MealPreferenceExtractor.Preferences(
                    latest.mealTime() == null ? previous.mealTime() : latest.mealTime(),
                    latest.taste() == null ? previous.taste() : latest.taste());
        });

        return new Turn(sessionId, recommendationService.recommendForPreferences(merged));
    }

    public record Request(UUID sessionId, String message) {}
    public record Turn(UUID sessionId, RecommendationService.Result result) {}
}