package com.vehiqon.security.filter;

import com.vehiqon.common.api.dto.RequestContext;
import com.vehiqon.common.api.rateLimit.RateLimitService;
import com.vehiqon.common.api.rateLimit.config.RateLimitProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {
    private final ObjectProvider<RequestContext> requestContextProvider;
    private final RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String clientIp = request.getRemoteAddr();
        RateLimitProperties props = rateLimitService.getRateLimitProperties();
        RateLimitProperties.RateLimitConfig config;
        String key;

        switch (path) {
          case  String p when p.endsWith("/auth/login") -> {
              config = props.getLogin();
              key = "LOGIN:" + clientIp;
          }
          case String p when p.endsWith("/auth/register") -> {
              config = props.getRegister();
              key = "REGISTER:" + clientIp;
          }
          case String p when p.endsWith("/auth/verify-email") -> {
                config = props.getVerifyEmail();
                key = "VERIFY_EMAIL:" + clientIp;
          }
          case String p when p.endsWith("/auth/resend-verification-email") -> {
                config = props.getResendVerificationEmail();
                key = "RESEND_VERIFY_EMAIL:" + clientIp;
          }
          case String p when p.endsWith("/auth/forgot-password") -> {
                config = props.getForgotPassword();
                key = "FORGOT_PASSWORD:" + clientIp;
          }
          case String p when p.endsWith("/auth/reset-password") -> {
                config = props.getResetPassword();
                key = "RESET_PASSWORD:" + clientIp;
          }
            default -> {
              String authHeader = request.getHeader("Authorization");
              if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                  config = props.getAuthenticatedEndPoint();
                  key = "AUTH_USER:" + authHeader.substring(7);
              } else {
                  config = props.getUnAuthenticatedEndPoint();
                  key = "PUBLIC_IP:" + clientIp;
              }
            }
        };

        if(!rateLimitService.tryConsume(key, config)) {
            handleRateLimitExceeded(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleRateLimitExceeded(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestContext requestContext = requestContextProvider.getObject();
        String requestId = requestContext != null ? requestContext.getRequestId() : null;
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(String.format("""
                {
                                "success": false,
                                "responseCode": "429",
                                "message": "Too Many Requests",
                                "error": {
                                  "code": "RATE_LIMIT_EXCEEDED",
                                  "message": "Too many requests on this action. Please try again later."
                                },
                                "path": "%s",
                                "requestId": "%s"
                              }
                """, request.getRequestURI(), requestId != null ? requestId : ""));
    }

//    private String resolveClientKey(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//        if(!authHeader.isBlank() && authHeader.startsWith("Bearer")) {
//            return authHeader.substring(7);
//        }
//        return request.getRemoteAddr();
//    }
}
