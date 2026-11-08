package com.diet.learning;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MealMapper {
    List<Meal> search(@Param("mealTime") String mealTime,
                      @Param("taste") String taste);
}