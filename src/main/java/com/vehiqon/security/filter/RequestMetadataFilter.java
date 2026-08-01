package com.vehiqon.security.filter;

import com.vehiqon.common.dto.RequestContext;
import com.vehiqon.security.config.UserAgentParserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
        userAgentParserService.parse(request, requestContext);
        filterChain.doFilter(request, response);
    }
}
