package com.vehiqon.security.config;

import com.vehiqon.common.dto.RequestContext;
import com.vehiqon.common.utils.HttpRequestUtils;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAgentParserService {
    private final HttpRequestUtils httpRequestUtils;
    private UserAgentAnalyzer uaa;
//    private DatabaseReader geoDatabaseReader;

    @PostConstruct
    public void init() {
        this.uaa = UserAgentAnalyzer.newBuilder()
                .hideMatcherLoadStats()
                .withCache(10000)
                .withFields(
                        UserAgent.AGENT_NAME,
                        UserAgent.AGENT_VERSION,
                        UserAgent.OPERATING_SYSTEM_NAME_VERSION_MAJOR,
                        UserAgent.DEVICE_CLASS,
                        UserAgent.LAYOUT_ENGINE_CLASS
                )
                .build();
        uaa.parse("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/138.0.0.0 Safari/537.36");
    }

    public void parse(HttpServletRequest request, RequestContext requestContext) {
        if (request == null || requestContext == null) {
            return;
        }
        String deviceId = request.getHeader("X-Device-Id");
        requestContext.setDeviceId(deviceId);

        String clientIp = httpRequestUtils.getClientIp(request);
        boolean isLocalhost = "127.0.0.1".equals(clientIp)
                || "0:0:0:0:0:0:0:1".equals(clientIp)
                || "::1".equals(clientIp);
        String city = isLocalhost ? "Localhost" : "Unknown";
        String country = isLocalhost ? "Localhost" : "Unknown";
        requestContext.setCity(city);
        requestContext.setCountry(country);
        requestContext.setIpAddress(clientIp);

        String userAgentString = request.getHeader("User-Agent");
        if (userAgentString == null || userAgentString.isBlank()) {
            applyUnknownUserAgentDefaults(requestContext);
            return;
        }

        UserAgent agent = uaa.parse(userAgentString);
        String browser = defaultIfUnknown(agent.getValue(UserAgent.AGENT_NAME));
        String os = defaultIfUnknown(agent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION_MAJOR));
        String appVersion = defaultIfUnknown(agent.getValue(UserAgent.AGENT_VERSION));

        String engineClass = agent.getValue(UserAgent.LAYOUT_ENGINE_CLASS);
        String deviceClass = agent.getValue(UserAgent.DEVICE_CLASS);

        String engine = engineClass == null ? "" : engineClass.toLowerCase(Locale.ROOT);
        String device = deviceClass == null ? "" : deviceClass.toLowerCase(Locale.ROOT);

        String platform =switch (engine) {
            case "mobile app" -> "Mobile App";
            case "browser" ->"phone".equals(device) ? "Mobile Web" : "Desktop Web";
            default -> engineClass;
        };

        requestContext.setBrowser(browser);
        requestContext.setAppVersion(appVersion);
        requestContext.setPlatform(platform);
        requestContext.setDevice(device);
        requestContext.setDeviceName(defaultIfUnknown(deviceClass));
        requestContext.setOperatingSystem(os);


    }

    private static void applyUnknownUserAgentDefaults(RequestContext requestContext) {
        requestContext.setBrowser("Unknown");
        requestContext.setAppVersion("Unknown");
        requestContext.setPlatform("Unknown");
        requestContext.setDevice("Unknown");
        requestContext.setDeviceName("Unknown");
        requestContext.setOperatingSystem("Unknown");
    }

    private String defaultIfUnknown(String value) {
        if(value == null || value.isBlank()) {return "Unknown";}
        return value;
    }


}
