package com.vehiqon.security.authorization;

import com.vehiqon.common.api.dto.RequestContext;
import com.vehiqon.common.api.dto.response.ApiError;
import com.vehiqon.common.api.dto.response.ErrorDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private final ObjectProvider<RequestContext> requestContextProvider;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        RequestContext requestContext = requestContextProvider.getObject();
        String requestId = requestContext != null ? requestContext.getRequestId() : null;
        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("ACCESS_DENIED")
                .message("You do not have permission to access this resource")
                .details(accessDeniedException.getMessage())
                .build();
        ApiError<ErrorDetail> apiError = ApiError.<ErrorDetail>builder()
                .success(false)
                .responseCode(String.valueOf(HttpStatus.FORBIDDEN.value()))
                .message("Forbidden")
                .error(errorDetail)
                .path(request.getRequestURI())
                .requestId(requestId)
                .build();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), apiError);
    }
}
