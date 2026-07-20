package com.vehiqon.features.insights.dashboard.service;

public interface DashboardService {
    void getMostUsedFeatures();

    void getFeatureUsage();

    void getWeeklyUsage();

    void getMonthlyUsage();

    void getSessionDuration();

    void getRetention();

    void getActivityHeatmap();

    void getRecentActivities();
}
