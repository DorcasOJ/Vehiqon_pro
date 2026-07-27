package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.service.UserAgentParserService;
import com.vehiqon.features.insights.Notification.dto.NotificationDto;
import com.vehiqon.features.insights.Notification.enums.NotificationEvent;
import com.vehiqon.features.insights.analytics.dto.AnalyticsDto;
import com.vehiqon.features.insights.auditLog.enums.AuditAction;
import com.vehiqon.features.insights.auditLog.enums.AuditStatus;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.enums.VerificationTokenTypeEnum;
import com.vehiqon.common.exception.*;
import com.vehiqon.features.insights.enums.PublishAction;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.analytics.service.AnalyticsService;
import com.vehiqon.features.insights.auditLog.dto.AuditLogDto;
import com.vehiqon.features.insights.auditLog.service.AuditLogService;
import com.vehiqon.features.onboarding.config.RateLimitProperties;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.service.RateLimitService;
import com.vehiqon.common.utils.GenerateOrHashTokenUtils;
import com.vehiqon.common.utils.HttpRequestUtils;
import com.vehiqon.features.onboarding.dto.request.*;
import com.vehiqon.features.onboarding.dto.response.LoginResponse;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.PasswordResetTokenEntity;
import com.vehiqon.features.onboarding.entity.RefreshTokenEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.entity.VerificationTokenEntity;
import com.vehiqon.features.onboarding.mapper.*;
import com.vehiqon.features.onboarding.repository.PasswordResetTokenRepository;
import com.vehiqon.features.onboarding.repository.RefreshTokenRepository;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.repository.VerificationTokenRepository;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.features.onboarding.service.UserService;
import com.vehiqon.security.config.CustomAuthenticationDetails;
import com.vehiqon.security.jwt.JwtService;
import com.vehiqon.security.model.CustomerUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final RateLimitProperties rateLimitProperties;
    private final HttpRequestUtils httpRequestUtils;
    private final InsightEventPublisher publisher;
    private final AnalyticsService analyticsService;
    private final UserAgentParserService userAgentParserService;


    @Override
    @Transactional
    public UserDto.UserResponse register(UserDto.CreateUserRequest request, HttpServletRequest httpServletRequest) {
        rateLimitService.validateRegister(request.email(),httpRequestUtils.getClientIp(httpServletRequest) );
        UserDto.UserResponse user = userService.createUser(request);

        publisher.publish( new AuditLogDto.AuditEvent(user.id(), AuditAction.USER_REGISTERED, EntityEnum.USER,
                user.id(), AuditStatus.SUCCESS, httpServletRequest, PublishAction.AUDIT_LOG));
        return user;
    }

    @Override
    public LoginResponse login(AuthDto.LoginRequest request, HttpServletRequest
            httpRequest) {
        long start = System.nanoTime();
        String email = request.email();
        rateLimitService.validateLogin(request.email(),httpRequestUtils.getClientIp(httpRequest) );
//        log.info("login rate limit = {} ms", (System.nanoTime() - start) / 1_000_000);

        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
//        log.info("get user Opt = {} ms", (System.nanoTime() - start) / 1_000_000);

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email, request.password()
                    )
            );
//            log.info("authenticate = {} ms", (System.nanoTime() - start) / 1_000_000);

            UserEntity user = (UserEntity) authenticate.getPrincipal();
//            log.info("get user principal = {} ms", (System.nanoTime() - start) / 1_000_000);

            resetLoginAttempts(Objects.requireNonNull(user));
//            log.info("reset login attempt = {} ms", (System.nanoTime() - start) / 1_000_000);


            AnalyticsDto.SessionContext context = userAgentParserService.parseRequestDetails(httpRequest);
//            log.info("get session context = {} ms", (System.nanoTime() - start) / 1_000_000);

            UUID userSessionId = UUID.randomUUID();
            analyticsService.startUserSession(userSessionId, user.getId(), context);
//            log.info("start session for user = {} ms", (System.nanoTime() - start) / 1_000_000);

            LoginResponse response = generateTokens(user, httpRequest, userSessionId);
//            log.info("generate user tokens = {} ms", (System.nanoTime() - start) / 1_000_000);

            publisher.publish(new AuditLogDto.AuditEvent(user.getId(), AuditAction.USER_LOGGED_IN, EntityEnum.USER,
                        user.getId(), AuditStatus.SUCCESS, httpRequest, PublishAction.AUDIT_LOG));
//            log.info("audit log for login = {} ms", (System.nanoTime() - start) / 1_000_000);

            log.debug("user session id: {}", userSessionId);
            response.setDeviceId(context.deviceId());
//            log.info("set device Id before returning = {} ms", (System.nanoTime() - start) / 1_000_000);

            return response;

        } catch (BadCredentialsException e) {
            userOpt.ifPresent(
                    userFound ->
                        handleFailedLoginAttempt(httpRequest, userFound));
            throw new InvalidResourceException("Invalid email or password.");
        } catch (LockedException ex) {
            throw new AccountLockedException("Account is temporarily locked. Try again later.");
        } catch (DisabledException ex) {
            throw new AccountDisabledException("Your account is not active. Please verify your email to continue.");
//            If an account with this email exists and is not yet verified, a new verification email has been sent.
        } catch (CredentialsExpiredException ex) {
            throw new com.vehiqon.common.exception.CredentialsExpiredException(ex.getMessage());
        }

    }


    @Override
    public LoginResponse refresh(AuthDto.RefreshTokenRequest request, HttpServletRequest httpRequest) {

        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(tokenUtils.hashToken(request.refreshToken()))
                .orElseThrow(() -> new InvalidResourceException("Refresh token not found"));
        if(Boolean.TRUE.equals(refreshToken.getRevoked())) {
            throw new InvalidResourceException("Refresh token has been revoked");
        }

        UUID sessionId = jwtService.extractSessionId(refreshToken.getToken());
        if(Boolean.TRUE.equals(refreshToken.getExpired())) {
            analyticsService.endSession(sessionId);
            throw new InvalidResourceException("Refresh token has expired");
        }

        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);
            analyticsService.endSession(sessionId);
            throw new InvalidResourceException("Refresh token has expired");
        }

        UserEntity user = userRepository.findById(refreshToken.getUserId()).orElseThrow(() -> new ResourceNotFoundException("user does not exist"));
        LoginResponse response = generateTokens(user, httpRequest, sessionId);
        refreshTokenRepository.revokeToken(refreshToken.getToken());
        return response;

    }


    @Override
    @Transactional
    public String verifyEmail(String token, HttpServletRequest httpServletRequest) {
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
        rateLimitService.validateVerifyEmail(user.getEmail(), httpRequestUtils.getClientIp(httpServletRequest));
        userRepository.markUserAsIsVerified(user.getId());
        verificationTokenRepository.markAllAsUsed(user.getId(), VerificationTokenTypeEnum.EMAIL_VERIFICATION, LocalDateTime.now());

        publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditAction.USER_EMAIL_VERIFIED, EntityEnum.USER,
                user.getId(), AuditStatus.SUCCESS, httpServletRequest, PublishAction.AUDIT_LOG));

        return "Email verified successfully.";
    }


    @Override
    @Transactional
    public String resendVerificationEmail(
                    AuthDto.ResendVerificationRequest request,
                    HttpServletRequest httpServletRequest
    ) {
        Optional<UserEntity> optionalUser =
                    userRepository.findByEmail(request.email());

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
        publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditAction.USER_VERIFICATION_EMAIL_RESENT, EntityEnum.USER,
                user.getId(), AuditStatus.SUCCESS, httpServletRequest, PublishAction.AUDIT_LOG));

            return "If an account exists with this email, a verification email has been sent.";
        }

    @Override
    public CustomerUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findByEmail(Objects.requireNonNull(userDetails).getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (authentication.getDetails() instanceof CustomAuthenticationDetails details) {
            String sessionId = details.getSessionId();
            String jti = details.getJti();

            return new CustomerUserDetails(user, UUID.fromString(Objects.requireNonNull(sessionId)),
                    UUID.fromString(jti), user.getAuthorities());
        } else {
            throw new BadRequestException("SessionId not Found for user");
        }

    }


    @Override
    public String logout(AuthDto.LogoutRequest request, HttpServletRequest httpServletRequest) {
        CustomerUserDetails authenticatedUser = getAuthenticatedUser();
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));
        refreshToken.setRevoked(true);
        refreshToken.setExpired(true);
        refreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
        analyticsService.endSession(authenticatedUser.sessionId());
        publisher.publish( new AuditLogDto.AuditEvent(refreshToken.getUserId(), AuditAction.USER_LOGGED_OUT, EntityEnum.USER,
                refreshToken.getUserId(), AuditStatus.SUCCESS, httpServletRequest, PublishAction.AUDIT_LOG));
        return "Logged out Successful";
    }

    @Override
    @Transactional
    public String logoutAll(HttpServletRequest httpServletRequest) {
        CustomerUserDetails authenticatedUser = getAuthenticatedUser();
        UserEntity user = authenticatedUser.user();
        refreshTokenRepository.revokeAll(user.getId());
//        log.info("Revoked refresh tokens for user {}",user.getEmail());
        analyticsService.endAllSession(authenticatedUser.sessionId(), user.getId());
          publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditAction.USER_LOGGED_OUT_ALL, EntityEnum.USER,
                user.getId(), AuditStatus.SUCCESS, httpServletRequest, PublishAction.AUDIT_LOG));

        return "Logged out from all devices";
    }

    @Override
    public String changePassword(AuthDto.ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
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
        publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditAction.USER_PASSWORD_CHANGED, EntityEnum.USER,
                user.getId(), AuditStatus.SUCCESS, httpServletRequest, PublishAction.AUDIT_LOG));

        return "Password changed successfully";
    }

    @Override
    public String forgotPassword(AuthDto.ForgotPasswordRequest request, HttpServletRequest httpServletRequest) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.email());
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
            publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditAction.USER_PASSWORD_RESET_REQUESTED, EntityEnum.USER,
                    user.getId(), AuditStatus.SUCCESS, httpServletRequest, PublishAction.AUDIT_LOG));
        });
        return "If an account with that email exists, a password reset link has been sent.";
    }

    @Override
    @Transactional
    public String resetPassword(AuthDto.ResetPasswordRequest request, HttpServletRequest httpServletRequest) {
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
        publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditAction.USER_PASSWORD_RESET_COMPLETED, EntityEnum.USER,
                user.getId(), AuditStatus.SUCCESS, httpServletRequest, PublishAction.AUDIT_LOG));

        return "Password has been successfully updated. You can now log in with your new password.";
    }

    private LoginResponse generateTokens(UserEntity user,  HttpServletRequest request, UUID sessionId) {
        try {
            long start = System.nanoTime();
            Map<String, Object> claims = new HashMap<>();
            claims.put("sessionId", sessionId.toString());

            String refreshToken = jwtService.generateRefreshToken(user, claims);
            log.info("generate refresh token = {} ms", (System.nanoTime() - start) / 1_000_000);

            refreshTokenRepository.save(
                    jwtService.mapRefreshTokenToEntity(tokenUtils.hashToken(refreshToken), user, request, sessionId)
            );
            log.info("save refresh token = {} ms", (System.nanoTime() - start) / 1_000_000);


            publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditAction.USER_REFRESHED_TOKEN, EntityEnum.USER,
                    user.getId(), AuditStatus.SUCCESS, request, PublishAction.AUDIT_LOG));
            log.info("audit log in token generation = {} ms", (System.nanoTime() - start) / 1_000_000);

            claims.put(
                    "roles",
                    user.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList()
            );
            String accessToken = jwtService.generateToken(user, claims);
            log.info("generate access token = {} ms", (System.nanoTime() - start) / 1_000_000);

            return loginResponseMapper.toLoginResponse(accessToken, refreshToken, user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void handleFailedLoginAttempt(HttpServletRequest httpRequest, UserEntity userFound) {
        userFound.incrementFailedLoginAttempt();


        if (userFound.getFailedLoginAttempts() >= maxFailedAttempt) {
            userFound.lock(Duration.ofMinutes(lockAccountBasedOnFailedAttemptInMinute));
            publisher.publish( new AuditLogDto.AuditEvent(userFound.getId(), AuditAction.ACCOUNT_LOCKED, EntityEnum.USER,
                    userFound.getId(), AuditStatus.FAILED, httpRequest, PublishAction.AUDIT_LOG));
            userFound.setFailedLoginAttempts(0);
        }
        userRepository.save(userFound);

        auditLogService.log( new AuditLogDto.AuditEvent(userFound.getId(), AuditAction.LOGIN_FAILED, EntityEnum.USER,
                userFound.getId(), AuditStatus.FAILED, httpRequest, PublishAction.AUDIT_LOG));
    }

    private void resetLoginAttempts(UserEntity user) {
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        userRepository.save(user);
    }

}
