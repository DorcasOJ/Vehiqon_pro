package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.dto.RequestContext;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.enums.RoleEnum;
import com.vehiqon.common.enums.VerificationTokenTypeEnum;
import com.vehiqon.common.exception.*;
import com.vehiqon.common.utils.GenerateOrHashTokenUtils;
import com.vehiqon.common.utils.HttpRequestUtils;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.Notification.dto.NotificationDto;
import com.vehiqon.features.insights.Notification.enums.NotificationEvent;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.analytics.mapper.AnalyticsMapper;
import com.vehiqon.features.insights.analytics.service.AnalyticService;
import com.vehiqon.features.insights.auditLog.dto.AuditLogDto;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.enums.AuditStatus;
import com.vehiqon.features.insights.auditLog.service.AuditLogService;
import com.vehiqon.features.insights.auditLog.service.RequestedMetadataService;
import com.vehiqon.features.insights.enums.PublishAction;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.request.AuthDto;
import com.vehiqon.features.onboarding.dto.response.LoginResponse;
import com.vehiqon.features.onboarding.entity.PasswordResetTokenEntity;
import com.vehiqon.features.onboarding.entity.RefreshTokenEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.entity.VerificationTokenEntity;
import com.vehiqon.features.onboarding.mapper.AuthMapper;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.repository.PasswordResetTokenRepository;
import com.vehiqon.features.onboarding.repository.RefreshTokenRepository;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.repository.VerificationTokenRepository;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.features.onboarding.service.RateLimitService;
import com.vehiqon.features.onboarding.service.UserService;
import com.vehiqon.security.config.JwtProperties;
import com.vehiqon.security.service.JwtService;
import com.vehiqon.security.model.CustomerUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${RESET_PASSWORD_URL}")
    private String resetPasswordLink;

    @Value("${LOCK_ACCOUNT_MINUTE}")
    private Integer lockAccountBasedOnFailedAttemptInMinute;

    @Value("${MAX_LOGIN_FAILED_ATTEMPT}")
    private Integer maxFailedAttempt;

    private final UserMapper userMapper;
    private final AuthMapper loginResponseMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final GenerateOrHashTokenUtils tokenUtils;
    private final AuditLogService auditLogService;
    private final RateLimitService rateLimitService;
    private final HttpRequestUtils httpRequestUtils;
    private final InsightEventPublisher publisher;
    private final AnalyticService analyticService;
    private final RequestContext requestContext;
    private final HttpServletRequest httpServletRequest;
    private final RequestedMetadataService metadataService;
    private final JwtProperties jwtProperties;
    private final AnalyticsMapper analyticsMapper;


    @Override
    @Transactional
    public UserDto.UserResponse register(UserDto.CreateUserRequest request) {
        rateLimitService.validateRegister(request.email(),httpRequestUtils.getClientIp(httpServletRequest) );
        UserDto.UserResponse user = userService.createUser(request);
        Map<String, Object> metadata = metadataService.createMetadata();
        publisher.publish( new AuditLogDto.AuditEvent(user.id(), AuditActionType.USER_REGISTERED, EntityEnum.USER,
                user.id(), AuditStatus.SUCCESS, PublishAction.AUDIT_LOG, metadata));
        return user;
    }

    @Override
    public LoginResponse login(AuthDto.LoginRequest request) {
        String email = request.email();
        rateLimitService.validateLogin(request.email(),httpRequestUtils.getClientIp(httpServletRequest) );
        Optional<UserEntity> userOpt = userRepository.findByEmailAndDeletedFalse(email);

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email, request.password()
                    )
            );
            UserEntity user = (UserEntity) authenticate.getPrincipal();
            resetLoginAttempts(Objects.requireNonNull(user));

            UUID userSessionId = UUID.randomUUID();
            Map<String, Object> metadata = metadataService.createMetadata();
            if (requestContext.getDeviceId() == null || requestContext.getDeviceId().isEmpty()){
                requestContext.setDeviceId(UUID.randomUUID().toString());
            }
            AnalyticsDto.SessionContext sessionContext = requestContext.toSessionContext(user.getId(), userSessionId);
            analyticService.sessionForLogin(sessionContext, metadata);

            LoginResponse response = generateTokens(user, httpServletRequest, UUID.fromString(requestContext.getDeviceId()));
            publisher.publish(new AuditLogDto.AuditEvent(user.getId(), AuditActionType.USER_LOGGED_IN, EntityEnum.USER,
                        user.getId(), AuditStatus.SUCCESS,PublishAction.AUDIT_LOG, metadata));

            log.debug("user session id: {}", userSessionId);
            response.setDeviceId(requestContext.getDeviceId());

            return response;

        } catch (BadCredentialsException e) {
            userOpt.ifPresent(
                    userFound ->
                        handleFailedLoginAttempt(httpServletRequest, userFound));
            throw new InvalidResourceException("Invalid email or password.");
        } catch (LockedException ex) {
            throw new AccountLockedException("Account is temporarily locked. Try again later.");
        } catch (DisabledException ex) {
            throw new AccountDisabledException("Your account is not active. Please verify your email to continue.");
      } catch (CredentialsExpiredException ex) {
            throw new com.vehiqon.common.exception.CredentialsExpiredException(ex.getMessage());
        }

    }


    @Override
    public LoginResponse refresh(AuthDto.RefreshTokenRequest request) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(tokenUtils.hashToken(request.refreshToken()))
                .orElseThrow(() -> new InvalidResourceException("Refresh token not found"));
        if(Boolean.TRUE.equals(refreshToken.getRevoked())) {
            throw new InvalidResourceException("Refresh token has been revoked");
        }

        UUID deviceId = jwtService.extractDeviceId(refreshToken.getToken());

        if(Boolean.TRUE.equals(refreshToken.getExpired())) {
            analyticService.endSessionForLogout(refreshToken.getUserId(), deviceId);
            throw new InvalidResourceException("Refresh token has expired");
        }

        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);
            analyticService.endSessionForLogout(refreshToken.getUserId(), deviceId);
            throw new InvalidResourceException("Refresh token has expired");
        }

        UserEntity user = userRepository.findById(refreshToken.getUserId()).orElseThrow(() -> new ResourceNotFoundException("user does not exist"));
        LoginResponse response = generateTokens(user, httpServletRequest, deviceId);
        refreshTokenRepository.revokeToken(refreshToken.getToken());
        return response;

    }


    @Override
    @Transactional
    public String verifyEmail(String token) {
        VerificationTokenEntity verificationToken = verificationTokenRepository
                .findActiveToken(
                        tokenUtils.hashToken(token),
                        VerificationTokenTypeEnum.EMAIL_VERIFICATION
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Email Verification Failed.")
                );
        UserEntity user = userRepository.findById(verificationToken.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("Email Verification Failed, user not found"));
        Map<String, Object> metadata = metadataService.createMetadata();
        rateLimitService.validateVerifyEmail(user.getEmail(), (String) metadata.get("ip"));
        userRepository.markUserAsIsVerified(user.getId());
        verificationTokenRepository.markAllAsUsed(user.getId(), VerificationTokenTypeEnum.EMAIL_VERIFICATION, LocalDateTime.now());

        publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditActionType.USER_VERIFIED_EMAIL, EntityEnum.USER,
                user.getId(), AuditStatus.SUCCESS, PublishAction.AUDIT_LOG, metadata));

        return "Email verified successfully.";
    }


    @Override
    @Transactional
    public String resendVerificationEmail(
                    AuthDto.ResendVerificationRequest request
    ) {
        Optional<UserEntity> optionalUser =
                    userRepository.findByEmailAndDeletedFalse(request.email());

            if (optionalUser.isEmpty()) {
                return "If an account exists with this email, a verification email has been sent.";

            }

            UserEntity user = optionalUser.get();
            if (Boolean.TRUE.equals(user.getIsVerified())) {
                return "Email is already verified.";
            }

            rateLimitService.validateResendVerificationEmail(user.getEmail(),  httpRequestUtils.getClientIp(httpServletRequest));
        // invalidate previous unused tokens
            List<VerificationTokenEntity> tokens =
                    verificationTokenRepository.findByUserIdAndTypeAndUsedFalse(
                            user.getId(),
                            VerificationTokenTypeEnum.EMAIL_VERIFICATION
                    );
            for (VerificationTokenEntity token : tokens) {
                token.setUsed(true);
                token.setUsedAt(LocalDateTime.now());
            }
            verificationTokenRepository.saveAll(tokens);
            userService.validateUserEmail(user);
        Map<String, Object> metadata = metadataService.createMetadata();
        publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditActionType.USER_VERIFICATION_EMAIL_RESENT, EntityEnum.USER,
                user.getId(), AuditStatus.SUCCESS, PublishAction.AUDIT_LOG, metadata));

            return "If an account exists with this email, a verification email has been sent.";
        }


    @Override
    public CustomerUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("Authentication={}",
//                SecurityContextHolder.getContext().getAuthentication());
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findByEmailAndDeletedFalse(Objects.requireNonNull(userDetails).getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UUID sessionId = userDetails.deviceId();
        UUID jti = userDetails.jti();
        return new CustomerUserDetails(user, userDetails.deviceId(), userDetails.jti(), user.getAuthorities());

    }

    @Override
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Objects.requireNonNull(authentication).getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), RoleEnum.ROLE_ADMIN.name()));
    }


    @Override
    public String logout(AuthDto.LogoutRequest request) {
        CustomerUserDetails authenticatedUser = getAuthenticatedUser();

        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new ResourceNotFoundException( "Token", request.refreshToken()));
        refreshToken.setRevoked(true);
        refreshToken.setExpired(true);
        refreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
        analyticService.endSessionForLogout(authenticatedUser.user().getId(),authenticatedUser.deviceId());
//        publisher.publish( new AuditLogDto.AuditEvent(refreshToken.getUserId(), AuditActionType.USER_LOGGED_OUT, EntityEnum.USER,
//                refreshToken.getUserId(), AuditStatus.SUCCESS, context.request(), Map.of(), PublishAction.AUDIT_LOG));
        return "Logged out Successful";
    }

    @Override
    @Transactional
    public String logoutAll() {
        CustomerUserDetails authenticatedUser = getAuthenticatedUser();
        UserEntity user = authenticatedUser.user();
        refreshTokenRepository.revokeAll(user.getId());

//        log.info("Revoked refresh tokens for user {}",user.getEmail());
//          publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditActionType.USER_LOGGED_OUT_ALL, EntityEnum.USER,
//                user.getId(), AuditStatus.SUCCESS, context.request(), PublishAction.AUDIT_LOG));
        analyticService.endAllSessionWithDeviceId(authenticatedUser.deviceId(), user.getId());

        return "Logged out from all devices";
    }

    @Override
    public String changePassword(AuthDto.ChangePasswordRequest request) {
        CustomerUserDetails authenticatedUser = getAuthenticatedUser();
        UserEntity user = authenticatedUser.user();

        if(!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if(!request.newPassword().equals(request.confirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if(passwordEncoder.matches(request.newPassword(), authenticatedUser.user().getPassword())) {
            throw new BadRequestException("New password must be different");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        // revoking all refresh tokens
        refreshTokenRepository.revokeAll(user.getId());

        return "Password changed successfully";
    }

    @Override
    public String forgotPassword(AuthDto.ForgotPasswordRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmailAndDeletedFalse(request.email());
        rateLimitService.validateForgotPassword(request.email(), httpRequestUtils.getClientIp(httpServletRequest) );
        userOpt.ifPresent(user ->{
            passwordResetTokenRepository.markAllAsUsedByUserId(user.getId());
            String token = tokenUtils.generateSecureToken(32);
            PasswordResetTokenEntity savedPasswordToken = passwordResetTokenRepository.save(authMapper.toPasswordResetTokenEntity(user, tokenUtils.hashToken(token)));
//            resetLoginAttempts(user);
            userRepository.save(user);
            String link = resetPasswordLink + token;
            log.debug("forgot password token here {}", token); // remove this later

            publisher.publish(new NotificationDto.ResetPassword(PublishAction.NOTIFICATION, user.getId(),
                    user.getEmail(), link, NotificationEvent.RESET_PASSWORD));
            Map<String, Object> metadata = metadataService.createMetadata();
            publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditActionType.USER_PASSWORD_RESET_REQUESTED, EntityEnum.USER,
                    user.getId(), AuditStatus.SUCCESS, PublishAction.AUDIT_LOG, metadata));
        });
        return "If an account with that email exists, a password reset link has been sent.";
    }

    @Override
    @Transactional
    public String resetPassword(AuthDto.ResetPasswordRequest request) {
        PasswordResetTokenEntity token = passwordResetTokenRepository.findValidToken(tokenUtils.hashToken(request.token())).orElseThrow(() ->
                new BadRequestException("Invalid Token. Unable to reset password"));

        if(!request.newPassword().equals(request.confirmPassword())) {
            throw new BadRequestException("Password Reset Failed, Passwords do not match");
        }
        UserEntity user = userRepository.findById(token.getUserId()).orElseThrow(() ->
                new BadRequestException("Password Reset Failed, User not found"));

        rateLimitService.validateResetPassword(user.getEmail(), httpRequestUtils.getClientIp(httpServletRequest));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.markAllAsUsedByUserId(user.getId());
        refreshTokenRepository.revokeAll(user.getId());  // revoking all refresh tokens
        resetLoginAttempts(user);
        Map<String, Object> metadata = metadataService.createMetadata();
        publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditActionType.USER_PASSWORD_RESET_COMPLETED, EntityEnum.USER,
                user.getId(), AuditStatus.SUCCESS,PublishAction.AUDIT_LOG, metadata));

        return "Password has been successfully updated. You can now log in with your new password.";
    }

    private LoginResponse generateTokens(UserEntity user, HttpServletRequest request, UUID deviceId) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("deviceId", deviceId.toString());

            String jti = UUID.randomUUID().toString();
            String refreshToken = jwtService.generateRefreshToken(user, claims, jti);
            RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                    .token(refreshToken)
                    .userId(user.getId())
                    .deviceName(requestContext.getDeviceName())
                    .deviceId(requestContext.getDeviceId())
                    .ipAddress(requestContext.getIpAddress())
                    .jti(jti)
                    .expiresAt(
                            LocalDateTime.now()
                                    .plus(Duration.ofMillis(jwtProperties.refreshExpiration()))
                    )
                    .revoked(false)
                    .expired(false)
                    .build();

            refreshTokenRepository.save(refreshTokenEntity);

            Map<String, Object> metadata = metadataService.createMetadata();
            publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditActionType.USER_REFRESHED_TOKEN, EntityEnum.USER,
                    user.getId(), AuditStatus.SUCCESS, PublishAction.AUDIT_LOG, metadata));

            claims.put(
                    "roles",
                    user.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList()
            );
            String accessToken = jwtService.generateToken(user, claims);
            return loginResponseMapper.toLoginResponse(accessToken, refreshToken, user);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private void handleFailedLoginAttempt(HttpServletRequest httpRequest, UserEntity userFound) {
        userFound.incrementFailedLoginAttempt();
        Map<String, Object> metadata = metadataService.createMetadata();
        if (userFound.getFailedLoginAttempts() >= maxFailedAttempt) {
            userFound.lock(Duration.ofMinutes(lockAccountBasedOnFailedAttemptInMinute));
            publisher.publish( new AuditLogDto.AuditEvent(userFound.getId(), AuditActionType.ACCOUNT_LOCKED, EntityEnum.USER,
                    userFound.getId(), AuditStatus.FAILED, PublishAction.AUDIT_LOG, metadata));
            userFound.setFailedLoginAttempts(0);
        }
        userRepository.save(userFound);
        auditLogService.log( new AuditLogDto.AuditEvent(userFound.getId(), AuditActionType.LOGIN_FAILED, EntityEnum.USER,
                userFound.getId(), AuditStatus.FAILED,PublishAction.AUDIT_LOG, metadata));
    }

    private void resetLoginAttempts(UserEntity user) {
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        userRepository.save(user);
    }

}
