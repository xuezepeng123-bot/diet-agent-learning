package com.diet.learning;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//接收Http请求，把参数交给查询服务
@RestController
public class MealController {
    private final MealSearchService mealSearchService;

    public MealController(MealSearchService mealSearchService) {
        this.mealSearchService = mealSearchService;
    }

    @GetMapping("/api/meals")
    public List<Meal> search(
        @RequestParam(name = "mealTime", required = false) String mealTime,
        @RequestParam(name = "taste", required = false) String taste
    ) {
        return mealSearchService.search(mealTime, taste);
    }
}