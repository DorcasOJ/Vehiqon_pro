package com.vehiqon.common.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.utils.HttpRequestUtils;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Column;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
        // Warm up the parser
        uaa.parse("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/138.0.0.0 Safari/537.36");

//        try (InputStream dbStream = new ClassPathResource("GeoLite2-City.mmdb").getInputStream()){
//            this.geoDatabaseReader = new DatabaseReader.Builder(dbStream).build();
//        } catch (IOException e) {
//            System.out.println("GeoIP Database not loaded: " + e.getMessage());
//            throw new BadRequestException(e.getMessage());
//        }
    }

    public AnalyticsDto.SessionContext parseRequestDetails(HttpServletRequest request) {
        long start = System.currentTimeMillis();
        String userAgentString = request.getHeader("User-Agent");
        if(userAgentString == null || userAgentString.isBlank()) {
            return null;
        }
        UserAgent agent = uaa.parse(userAgentString);
        String browser = agent.getValue(UserAgent.AGENT_NAME);
        String os = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION_MAJOR);

        String appVersion = agent.getValue(UserAgent.AGENT_VERSION).equals("??") ? "Unknown" : agent.getValue(UserAgent.AGENT_VERSION);

        String engineClass = agent.getValue(UserAgent.LAYOUT_ENGINE_CLASS);
        String deviceClass = agent.getValue(UserAgent.DEVICE_CLASS);

        String engine = engineClass == null ? "" : engineClass.toLowerCase(Locale.ROOT);
        String device = deviceClass == null ? "" : deviceClass.toLowerCase(Locale.ROOT);

        String platform;

        switch (engine) {
            case "mobile app":
                platform = "Mobile App";
                break;
            case "browser":
                platform = "phone".equals(device) ? "Mobile Web" : "Desktop Web";
            default:
                platform = engineClass;
        }

        String clientIp = httpRequestUtils.getClientIp(request);

//        try {
//            if(!"127.0.0.1".equals(clientIp) && !"0:0:0:0:0:0:0:1".equals(clientIp)) {
//                InetAddress ipAddress = InetAddress.getByName(clientIp);
//                CityResponse cityResponse = geoDatabaseReader.city(ipAddress);
//                metadata.put("city", cityResponse.city().name());
//                metadata.put("country", cityResponse.country().name());
//            } else {
//                metadata.put("city", "Localhost");
//                metadata.put("country", "Localhost");
//            }
//        } catch (IOException e) {
//            metadata.put("city", "Unknown");
//            metadata.put("country", "Unknown");
//            throw new BadRequestException(e.getMessage());
//        }
        String city; String country;
        if (!"127.0.0.1".equals(clientIp)) {
            city = "Localhost";
            country = "Localhost";
        } else {
           city = "Unknown";
           country = "Unknown";
        }

        String deviceId = request.getHeader("X-Device-Id");
        if(deviceId == null || deviceId.isBlank()) {
//            deviceId = request.getSession(true).getId();
            deviceId = UUID.randomUUID().toString();
        }
        log.info("Agent parser = {} ms", System.currentTimeMillis()-start);
        return new AnalyticsDto.SessionContext(clientIp,city,country,device, browser,
                platform,os, appVersion, deviceId);
    }


}
