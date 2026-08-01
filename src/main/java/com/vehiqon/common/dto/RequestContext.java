package com.vehiqon.common.dto;

import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Component
@Getter
@Setter
@RequestScope
public class RequestContext {
    private String ipAddress;
    private String city;
    private String country;
    private String device;
    private String browser;
    private String platform;
    private String operatingSystem;
    private String appVersion;
    private String deviceId;
    private String deviceName;

    public void copyFrom(RequestContext source) {
        if(source == null) return;
        this.ipAddress = source.getIpAddress();
        this.city = source.getCity();
        this.country = source.getCountry();
        this.device = source.getDevice();
        this.browser = source.getBrowser();
        this.platform = source.getPlatform();
        this.operatingSystem = source.getOperatingSystem();
        this.appVersion = source.getAppVersion();
        this.deviceId = source.getDeviceId();
        this.deviceName = source.getDeviceName();
    }

    public AnalyticsDto.SessionContext toSessionContext(UUID userId, UUID userSessionId) {
        return new AnalyticsDto.SessionContext(userId, userSessionId, this.ipAddress, this.city, this.country,
                this.device, this.browser, this.platform, this.operatingSystem, this.appVersion, this.deviceId, this.deviceName);
    }
}
