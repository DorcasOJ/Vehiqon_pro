package com.vehiqon.features.insights.analytics.dto.requestScope;

import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.enums.PublishAction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequestScope
@Getter
@Setter
public class AnalyticsContext {
    private final Map<String, Object> metadata = new HashMap<>();

    public void put(String key, Object value) {
        if(value != null) {
            metadata.put(key, value);
        }
    }

    public void recordSearch(String query, Integer result, Long totalResults,
                             Integer pageNumber, Integer pageSize){
        metadata.put("query", query);
        metadata.put("results",result);
        metadata.put("totalResults", totalResults);
        metadata.put("pageNumber", pageNumber);
        metadata.put("pageSize", pageSize);
    }

    public void recordPayment(BigDecimal amount, String gateway, String currency) {
        metadata.put("amount", amount);
        metadata.put("gateway", gateway);
        metadata.put("currency", currency);

    }
}
