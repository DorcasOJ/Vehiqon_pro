package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.enums.AuditAction;
import com.vehiqon.common.enums.AuditStatus;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.enums.VerificationTokenTypeEnum;
import com.vehiqon.common.exception.*;
import com.vehiqon.common.service.AuditLogService;
import com.vehiqon.features.onboarding.config.RateLimitProperties;
import com.vehiqon.features.onboarding.service.RateLimitService;
import com.vehiqon.common.utils.AccountUtils;
import com.vehiqon.common.utils.GenerateOrHashTokenUtils;
import com.vehiqon.common.utils.HttpRequestUtils;
import com.vehiqon.features.email.mapper.EmailResponseMapper;
import com.vehiqon.features.email.service.EmailService;
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
import com.vehiqon.security.jwt.JwtService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final EmailResponseMapper emailResponseMapper;
    private final EmailService emailService;
    private final GenerateOrHashTokenUtils tokenUtils;
    private final AuditLogService auditLogService;
    private final RateLimitService rateLimitService;
    private final RateLimitProperties rateLimitProperties;
    private final HttpRequestUtils httpRequestUtils;

    @Override
    public UserResponse register(UserDto.CreateUserRequest request, HttpServletRequest httpServletRequest) {
        rateLimitService.validateRegister(request.email(),httpRequestUtils.getClientIp(httpServletRequest) );
        UserResponse user = userService.createUser(request);
         auditLogService.log(user.getId(), AuditAction.USER_REGISTERED.name(),
                 EntityEnum.USER, user.getId(), AuditStatus.SUCCESS,
            AccountUtils.user_registered_description, httpServletRequest);
        return user;
    }

    @Override
    public LoginResponse login(AuthDto.LoginRequest request, HttpServletRequest
            httpRequest) {
        String email = request.email();
        rateLimitService.validateLogin(request.email(),httpRequestUtils.getClientIp(httpRequest) );

        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email, request.password()
                    )
            );
        UserEntity user = (UserEntity) authenticate.getPrincipal();
            resetLoginAttempts(Objects.requireNonNull(user));
            userRepository.save(user);
        LoginResponse response = generateTokens(user, httpRequest);
        auditLogService.log(user.getId(), AuditAction.LOGIN_SUCCESS.name(),
                EntityEnum.USER, user.getId(), AuditStatus.SUCCESS,
                AuditAction.LOGIN_SUCCESS.getDescription(), httpRequest);
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

        if(Boolean.TRUE.equals(refreshToken.getExpired())) {
            throw new InvalidResourceException("Refresh token has expired");
        }

        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);
            throw new InvalidResourceException("Refresh token has expired");
        }

        UserEntity user = userRepository.findById(refreshToken.getUserId()).orElseThrow(() -> new ResourceNotFoundException("user does not exist"));
        LoginResponse response = generateTokens(user, httpRequest);
        refreshToken.setRevoked(true);
        refreshToken.setExpired(true);
        refreshToken.setRevokedAt(LocalDateTime.now());
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

        auditLogService.log(user.getId(), AuditAction.USER_VERIFIED_EMAIL.name(),
                EntityEnum.VERIFICATION_TOKEN, user.getId(), AuditStatus.SUCCESS,
                AccountUtils.user_email_verified_description, httpServletRequest);

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

            auditLogService.log(user.getId(), AuditAction.USER_REQUESTED_VERIFICATION_EMAIL_TOKEN.name(),
                    EntityEnum.VERIFICATION_TOKEN, user.getId(), AuditStatus.SUCCESS,
                    AccountUtils.user_requested_email_verification_description, httpServletRequest);
            return "If an account exists with this email, a verification email has been sent.";
        }

    @Override
    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userRepository.findByEmail(Objects.requireNonNull(userDetails).getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }


    @Override
    public String logout(AuthDto.LogoutRequest request, HttpServletRequest httpServletRequest) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));
        refreshToken.setRevoked(true);
        refreshToken.setExpired(true);
        refreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);

        auditLogService.log(refreshToken.getUserId(), AuditAction.USER_LOGGED_OUT.name(),
                EntityEnum.USER, refreshToken.getUserId(), AuditStatus.SUCCESS,
                AccountUtils.user_logout_description, httpServletRequest);
        return "Logged out Successful";
    }

    @Override
    @Transactional
    public String logoutAll(HttpServletRequest httpServletRequest) {
        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();
        UserEntity user =
                (UserEntity) authentication.getPrincipal();
        refreshTokenRepository.revokeAll(user.getId());
//        log.info("Revoked refresh tokens for user {}",user.getEmail());
        auditLogService.log(user.getId(), AuditAction.USER_LOGGED_OUT_ALL.name(),
                EntityEnum.USER, user.getId(), AuditStatus.SUCCESS,
                AccountUtils.user_logout_all_description, httpServletRequest);
        return "Logged out from all devices";
    }

    @Override
    public String changePassword(AuthDto.ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        UserEntity user = getAuthenticatedUser();
        if(!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if(!request.newPassword().equals(request.confirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if(passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new BadRequestException("New password must be different");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        // revoking all refresh tokens
        refreshTokenRepository.revokeAll(user.getId());
        auditLogService.log(user.getId(), AuditAction.USER_PASSWORD_CHANGED.name(),
                EntityEnum.USER, user.getId(), AuditStatus.SUCCESS,
                AccountUtils.password_changed_description, httpServletRequest);
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
            emailService.sendEmailAlert(emailResponseMapper.toResetPassword(user, link));

            auditLogService.log(user.getId(), AuditAction.USER_PASSWORD_RESET_REQUESTED.name(),
                    EntityEnum.PASSWORD_RESET_TOKEN, savedPasswordToken.getId(), AuditStatus.SUCCESS,
                    AccountUtils.password_reset_requested_description, httpServletRequest);
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
        auditLogService.log(user.getId(), AuditAction.USER_PASSWORD_RESET_COMPLETED.name(),
                EntityEnum.USER, user.getId(),AuditStatus.SUCCESS,
                AccountUtils.password_reset_description, httpServletRequest);
        return "Password has been successfully updated. You can now log in with your new password.";
    }

    private LoginResponse generateTokens(UserEntity user,  HttpServletRequest request) {
        try {
            Map<String, Object> claims = new HashMap<>();

            claims.put(
                    "roles",
                    user.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList()
            );

            String refreshToken = jwtService.generateRefreshToken(user);
            RefreshTokenEntity refreshTokenEntity = jwtService.mapRefreshTokenToEntity(tokenUtils.hashToken(refreshToken), user, request);
            RefreshTokenEntity newrefreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);

            auditLogService.log(user.getId(), AuditAction.USER_REFRESHED_TOKEN.name(),
                    EntityEnum.REFRESH_TOKEN, newrefreshTokenEntity.getId(),AuditStatus.SUCCESS,
                    AccountUtils.token_refreshed_description, request);

            String accessToken = jwtService.generateToken(user, claims);

            return loginResponseMapper.toLoginResponse(accessToken, refreshToken, user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void handleFailedLoginAttempt(HttpServletRequest httpRequest, UserEntity userFound) {
        userFound.incrementFailedLoginAttempt();

        if (userFound.getFailedLoginAttempts() >= maxFailedAttempt) {
            userFound.lock(Duration.ofMinutes(lockAccountBasedOnFailedAttemptInMinute));

            auditLogService.log(userFound.getId(), AuditAction.ACCOUNT_LOCKED.name(),
                    EntityEnum.USER, userFound.getId(), AuditStatus.SUCCESS,
                    AuditAction.ACCOUNT_LOCKED.getDescription(), httpRequest);

            userFound.setFailedLoginAttempts(0);
        }
        userRepository.save(userFound);
        auditLogService.log(userFound.getId(), AuditAction.LOGIN_FAILED.name(),
                EntityEnum.USER, userFound.getId(), AuditStatus.FAILED,
                AuditAction.LOGIN_FAILED.getDescription(), httpRequest);
    }

    private static void resetLoginAttempts(UserEntity user) {
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
    }

}
