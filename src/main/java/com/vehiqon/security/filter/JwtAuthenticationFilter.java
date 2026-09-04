package com.vehiqon.security.filter;

import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.infrastructure.redis.RedisCacheService;
import com.vehiqon.features.insights.analytics.repository.UserSessionRepository;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.security.authentication.model.CustomerUserDetails;
import com.vehiqon.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RedisCacheService redisService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith(("Bearer "))) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(7);
        try {
            final String email = jwtService.extractUsername(jwt);

            if(email != null &&
                 SecurityContextHolder.getContext().getAuthentication() == null) {

                UUID deviceId = jwtService.extractDeviceId(jwt);
                String jti = jwtService.extractJti(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if(jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                    if (redisService.hasKey("BLACKLIST:ACCESS_TOKEN:"+ jti)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    CustomerUserDetails customerUserDetails = new CustomerUserDetails(
                            (UserEntity) userDetails, deviceId, UUID.fromString(jti), userDetails.getAuthorities()
                    );
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            customerUserDetails, null, customerUserDetails.getAuthorities());
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );
                    SecurityContextHolder.getContext()
                            .setAuthentication(authenticationToken);
                }
            }
        } catch (UsernameNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to authenticate JWT token: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);

    }



}
