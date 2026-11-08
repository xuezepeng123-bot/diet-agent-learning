package com.diet.learning;
//定义一条餐食的数据有哪些字段
public record Meal(long id, String name, String mealTime, String taste) {
}