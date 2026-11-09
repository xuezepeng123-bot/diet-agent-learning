package com.diet.learning;

import io.agentscope.core.model.DashScopeChatModel;
import io.agentscope.core.model.EndpointType;
import io.agentscope.core.model.GenerateOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DietChatModel {
    private final DashScopeChatModel model;

    public DietChatModel(@Value("${DASHSCOPE_API_KEY:}") String apiKey,
            @Value("${DASHSCOPE_BASE_URL:}") String baseUrl) {
        this.model = apiKey.isBlank() || baseUrl.isBlank() ? null : DashScopeChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName("qwen3.7-flash")
                .endpointType(EndpointType.MULTIMODAL)
                .enableThinking(false)
                .stream(false)
                .defaultOptions(GenerateOptions.builder().maxTokens(128).build())
                .build();
    }

    public DashScopeChatModel getModel() {
        return model;
    }
}