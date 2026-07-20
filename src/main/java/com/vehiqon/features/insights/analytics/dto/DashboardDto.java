package com.vehiqon.features.insights.analytics.dto;

import com.vehiqon.features.carmgmt.entities.CarEntity;

public class DashboardDto {
    private  DashboardDto() {}

    public record DashboardResponse(
            CarEntity Cars
//            UpcomingReminders,
//
//            UnreadNotifications,
//
//            Subscription status
//
//            Total maintenance cost
//
//            Recent activity
//
//            Most used feature
//
//            Weekly activity
//
//            Maintenance completion rate
//
//            Vehicles due this month
//
//            Quick actions

    ){}
}
