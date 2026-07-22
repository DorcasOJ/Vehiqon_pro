package com.vehiqon.security.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Getter
public class CustomAuthenticationDetails extends WebAuthenticationDetails {
    private final String sessionId;
    private final String jti;
    public CustomAuthenticationDetails(HttpServletRequest request, String sessionId, String jti) {
        super(request);
        this.sessionId = sessionId;
        this.jti = jti;
    }


}
