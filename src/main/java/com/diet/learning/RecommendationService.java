package com.diet.learning;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {
    private final MealSearchService mealSearchService;

    public RecommendationService(MealSearchService mealSearchService) {
        this.mealSearchService = mealSearchService;
    }

    public Result recommend(String message) {
        String text = message == null ? "" : message;
        String mealTime = firstMatch(text, "早餐", "午餐", "晚餐");
        String taste = firstMatch(text, "清淡", "微辣");

        if (mealTime == null) {
            return new Result("ASK", "你想吃早餐、午餐还是晚餐？", List.of());
        }
        if (taste == null) {
            return new Result("ASK", "你喜欢清淡还是微辣？", List.of());
        }

        List<Meal> meals = mealSearchService.search(mealTime, taste);
        if (meals.isEmpty()) {
            return new Result("NO_MATCH", "没有找到符合条件的餐食。", meals);
        }
        return new Result("RECOMMEND", "找到了符合条件的餐食。", meals);
    }

    private String firstMatch(String text, String... options) {
        for (String option : options) {
            if (text.contains(option)) {
                return option;
            }
        }
        return null;
    }

    public record Result(String status, String reply, List<Meal> meals) {}
}