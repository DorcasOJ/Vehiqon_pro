package com.vehiqon.features.insights.auditLog.service;

import com.vehiqon.common.utils.HttpRequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RequestedMetadataService {
    private final HttpServletRequest request;
    private final HttpRequestUtils httpRequestUtils;

    public Map<String, Object> createMetadata() {
        Map<String, Object> metadata = new HashMap<>();

        metadata.put("path", request.getRequestURI());
        metadata.put("method", request.getMethod());
        metadata.put("ip",httpRequestUtils.getClientIp(request));
        metadata.put("userAgent", request.getHeader("User-Agent"));
        return metadata;
    }
}
