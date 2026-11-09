package com.diet.learning;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MealConversationMapper {
    SessionRow findById(@Param("sessionId") String sessionId);

    int insert(@Param("sessionId") String sessionId,
               @Param("mealTime") String mealTime,
               @Param("taste") String taste);

    int mergePreferences(@Param("sessionId") String sessionId,
                         @Param("mealTime") String mealTime,
                         @Param("taste") String taste);

    record SessionRow(String sessionId, String mealTime, String taste) {}
}