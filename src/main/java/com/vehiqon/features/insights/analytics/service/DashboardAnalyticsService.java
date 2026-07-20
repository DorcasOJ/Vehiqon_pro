package com.vehiqon.features.insights.analytics.service;

import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.dto.DashboardDto;

import java.util.List;
import java.util.UUID;

public interface DashboardAnalyticsService {
    DashboardDto.DashboardResponse getDashboard(UUID userId);
    AnalyticsDto.FeatureUsage getFeatureUsage(UUID userId);
    List<AnalyticsDto.Activity> getRecentActivities(UUID userId);
    AnalyticsDto.UsageTrend getWeeklyUsage(UUID userId);
    AnalyticsDto.RecommendationSummary getRecommendations(UUID userId);

}
