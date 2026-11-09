package com.diet.learning;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MealConversationService {
    private final MealConversationMapper mapper;

    public MealConversationService(MealConversationMapper mapper) {
        this.mapper = mapper;
    }

    public void checkSession(UUID sessionId) {
        if (sessionId != null) {
            findRequired(sessionId.toString());
        }
    }

    @Transactional
    public SavedTurn saveTurn(UUID sessionId, MealPreferenceExtractor.Preferences latest) {
        if (sessionId == null) {
            UUID newId = UUID.randomUUID();
            mapper.insert(newId.toString(), latest.mealTime(), latest.taste());
            return new SavedTurn(newId, latest);
        }

        String id = sessionId.toString();
        mapper.mergePreferences(id, latest.mealTime(), latest.taste());

        var row = findRequired(id);
        var merged = new MealPreferenceExtractor.Preferences(row.mealTime(), row.taste());
        return new SavedTurn(sessionId, merged);
    }

    private MealConversationMapper.SessionRow findRequired(String sessionId) {
        var row = mapper.findById(sessionId);
        if (row == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "会话不存在");
        }
        return row;
    }

    public record SavedTurn(UUID sessionId, MealPreferenceExtractor.Preferences preferences) {}
}