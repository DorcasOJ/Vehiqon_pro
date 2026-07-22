package com.vehiqon.security.jwt;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.common.utils.GenerateOrHashTokenUtils;
import com.vehiqon.features.insights.analytics.repository.UserSessionRepository;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.repository.RefreshTokenRepository;
import com.vehiqon.security.model.CustomerUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserSessionRepository userSessionRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GenerateOrHashTokenUtils hashTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith(("Bearer "))) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(7);
            String email = jwtService.extractUsername(jwt);
            UUID sessionId = jwtService.extractSessionId(jwt);
            String jti = jwtService.extractJti(jwt);

            if(email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                boolean isSessionNotLoggedOut = userSessionRepository.existsByIdAndLogoutAtIsNull(sessionId);
//

                if(jwtService.isTokenValid(jwt, userDetails.getUsername()) && isSessionNotLoggedOut) {
                    CustomerUserDetails customerUserDetails = new CustomerUserDetails(
                            (UserEntity) userDetails, sessionId, UUID.fromString(jti), userDetails.getAuthorities()
                    );
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            customerUserDetails, null, customerUserDetails.getAuthorities());
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
//                            new CustomAuthenticationDetails(request, sessionId.toString(), jti)
                    );
                    SecurityContextHolder.getContext()
                            .setAuthentication(authenticationToken);
                }
            }
        } catch (IOException | ServletException e) {
            throw new BadRequestException(e.getMessage());
        } catch (UsernameNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        filterChain.doFilter(request, response);

    }
}
