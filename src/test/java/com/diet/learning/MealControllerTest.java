package com.diet.learning;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MealControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void findsOnlyLightDinner() throws Exception {
        mvc.perform(get("/api/meals")
                .param("mealTime", "晚餐")
                .param("taste", "清淡"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("清蒸鲈鱼"));
    }

    @Test
    void returnsEmptyListWhenNothingMatches() throws Exception {
        mvc.perform(get("/api/meals").param("mealTime", "早餐"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }
}