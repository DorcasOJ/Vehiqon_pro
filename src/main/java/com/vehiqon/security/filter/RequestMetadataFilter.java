package com.vehiqon.security.filter;

import com.vehiqon.common.api.dto.RequestContext;
import com.vehiqon.security.config.UserAgentParserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RequestMetadataFilter extends OncePerRequestFilter {

    private final UserAgentParserService userAgentParserService;
    private final ObjectProvider<RequestContext> requestContextProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        RequestContext requestContext = requestContextProvider.getObject();
//        if(!StringUtils.hasText(requestContext.getRequestId())) {
//            String headerRequestId = request.getHeader("X-Request-Id");
//            requestContext.setRequestId(StringUtils.hasText(headerRequestId) ? headerRequestId : UUID.randomUUID().toString());
//
//        }
        userAgentParserService.parse(request, requestContext);
        response.setHeader("X-Request-Id", requestContext.getRequestId());
        if(requestContext.getRequestId() != null) {
            response.setHeader("X-Request-Id", requestContext.getRequestId());
        }
        filterChain.doFilter(request, response);
    }
}
