package com.diet.learning;

import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.MsgRole;
import io.agentscope.core.model.DashScopeChatModel;
import io.agentscope.core.model.ExecutionConfig;
import io.agentscope.core.tool.Toolkit;
import java.time.Duration;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MealAgentController {
    private static final Logger log = LoggerFactory.getLogger(MealAgentController.class);
    private final DietChatModel chatModel;
    private final MealSearchTool mealSearchTool;

    public MealAgentController(DietChatModel chatModel, MealSearchTool mealSearchTool) {
        this.chatModel = chatModel;
        this.mealSearchTool = mealSearchTool;
    }

    @PostMapping("/api/agent/recommendations")
    public Map<String, String> recommend(@RequestBody RecommendationController.Request request) {
        String message = request.message();
        if (message == null || message.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请输入饮食需求");
        }

        DashScopeChatModel model = chatModel.getModel();
        if (model == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "百炼模型未配置");
        }

        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(mealSearchTool);

        ReActAgent agent = ReActAgent.builder()
                .name("meal_agent")
                .model(model)
                .toolkit(toolkit)
                .sysPrompt("""
                        你是饮食推荐助手。餐次只能是早餐、午餐、晚餐，口味只能是清淡、微辣。
                        缺少餐次或口味时追问。晚上对应晚餐，不辣对应清淡。
                        条件齐全时调用 search_meals 一次，并根据工具返回的结果简短回答。
                        没有匹配时如实告知。查询失败时说明查询失败。不要编造菜名。
                        """)
                .maxIters(2)
                .modelExecutionConfig(ExecutionConfig.builder()
                        .timeout(Duration.ofSeconds(10)).maxAttempts(1).build())
                .build();

        try {
            Msg reply = agent.call(Msg.builder().role(MsgRole.USER).textContent(message).build()).block();
            String text = reply == null ? null : reply.getTextContent();
            if (text != null && !text.isBlank()) {
                return Map.of("reply", text);
            }
        } catch (RuntimeException ex) {
            log.warn("Meal agent failed ({})", ex.getClass().getSimpleName());
        }
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "饮食助手暂时无法回答");
    }
}