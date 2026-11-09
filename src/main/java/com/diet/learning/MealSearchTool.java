package com.diet.learning;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MealSearchTool {
    private final MealSearchService mealSearchService;

    public MealSearchTool(MealSearchService mealSearchService) {
        this.mealSearchService = mealSearchService;
    }

    @Tool(name = "search_meals", description = "按餐次和口味从数据库查询餐食")//查菜工具，声明工具和用途
    public List<Meal> searchMeals(
            @ToolParam(name = "mealTime", required = true, description = "早餐、午餐或晚餐")//声明工具参数和用途
            String mealTime,
            @ToolParam(name = "taste", required = true, description = "清淡或微辣")
            String taste) {
        if (mealTime == null || mealTime.isBlank() || taste == null || taste.isBlank()) {
            throw new IllegalArgumentException("查询餐食需要餐次和口味");
        }
        List<Meal> meals = mealSearchService.search(mealTime, taste);
org.slf4j.LoggerFactory.getLogger(MealSearchTool.class)
        .info("search_meals returned {} meal(s)", meals.size());
return meals;
    }
}