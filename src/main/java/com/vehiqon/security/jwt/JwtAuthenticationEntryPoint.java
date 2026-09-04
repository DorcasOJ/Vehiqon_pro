package com.vehiqon.security.jwt;

import com.vehiqon.common.api.dto.RequestContext;
import com.vehiqon.common.api.dto.response.ApiError;
import com.vehiqon.common.api.dto.response.ApiResponse;
import com.vehiqon.common.api.dto.response.ErrorDetail;
import com.vehiqon.common.utils.AccountUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectProvider<RequestContext> requestContextProvider;

    @Override
    public void commence(@NotNull HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        RequestContext requestContext = requestContextProvider.getObject();
        String requestId = requestContext != null ? requestContext.getRequestId() : null;
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("UNAUTHORISED")
                .message("Full authentication is required to access this resource")
                .details(authException.getMessage())
                .build();
        ApiError<ErrorDetail> apiError = ApiError.<ErrorDetail>builder()
                .success(false)
                .responseCode(String.valueOf(HttpStatus.FORBIDDEN.value()))
                .message("Unauthorized")
                .error(errorDetail)
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        objectMapper.writeValue(response.getOutputStream(), apiError);
    }
}
