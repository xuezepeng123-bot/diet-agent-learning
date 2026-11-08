package com.diet.learning;

import java.util.List;
import org.springframework.stereotype.Service;
//存放样例数据，并执行筛选
@Service
public class MealSearchService {
    private final List<Meal> meals = List.of(
        new Meal(1, "清蒸鲈鱼", "晚餐", "清淡"),
        new Meal(2, "宫保鸡丁", "晚餐", "微辣"),
        new Meal(3, "番茄鸡蛋面", "午餐", "清淡")
    );

    public List<Meal> search(String mealTime, String taste) {
        return meals.stream()
            .filter(meal -> matches(meal.mealTime(), mealTime))//先筛餐时
            .filter(meal -> matches(meal.taste(), taste))//再筛口味
            .toList();
    }

    private boolean matches(String value, String filter) {
        return filter == null || filter.isBlank() || value.equals(filter.strip());//某个参数没传，就不按这个条件筛选
    }
}