package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.enums.AuditAction;
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
import io.github.bucket4j.Bucket;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${RESET_PASSWORD_URL}")
    private String resetPasswordLink;

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
        Bucket bucket = rateLimitService.resolveBucket(
                "REGISTER:" + request.email() + ":" + httpRequestUtils.getClientIp(httpServletRequest),
                rateLimitProperties.getRegister().getCapacity(),
                rateLimitProperties.getRegister().getDuration()
        );
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may register attempts");
        }
        UserResponse user = userService.createUser(request);
        auditLogService.log(user.getId(), AuditAction.USER_REGISTERED.name(),
                AccountUtils.user_entity, user.getId(), AccountUtils.SUCCESS_MESSAGE,
                AccountUtils.user_registered_description, httpServletRequest);

        return user;
    }

    @Override
    public LoginResponse login(AuthDto.LoginRequest request, HttpServletRequest
            httpRequest) {
        Bucket bucket = rateLimitService.resolveBucket(
                "LOGIN:" + request.email() + ":" + httpRequestUtils.getClientIp(httpRequest),
                rateLimitProperties.getLogin().getCapacity(),
                rateLimitProperties.getLogin().getDuration()
        );
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may login attempts");
        }
        Authentication authenticate = getAuthentication(request);
        UserEntity user = (UserEntity) authenticate.getPrincipal();
        auditLogService.log(user.getId(), AuditAction.USER_LOGGED_IN.name(),
                AccountUtils.user_entity, user.getId(), AccountUtils.SUCCESS_MESSAGE,
                AccountUtils.user_login_description, httpRequest);

        return generateTokens(user, httpRequest);

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
                .findByTokenAndType(
                        token,
                        VerificationTokenTypeEnum.EMAIL_VERIFICATION
                )
                .orElseThrow(() ->
//                        new ResourceNotFoundException("Invalid verification token")
                        new ResourceNotFoundException("Email Verification Failed, Invalid Token")
                );

        if (Boolean.TRUE.equals(verificationToken.getUsed())) {
//            throw new IllegalArgumentException("Verification link has already been used");
            throw new BadRequestException("Email Verification Failed, link already used");
        }

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
//            throw new IllegalArgumentException("Verification link has expired");
            throw new BadRequestException("Email Verification Failed, link expired");
        }
        UserEntity user = userRepository.findById(verificationToken.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("Email Verification Failed, user not found"));

        Bucket bucket = rateLimitService.resolveBucket(
                "VERIFY_EMAIL:" + user.getEmail()+ ":" + httpRequestUtils.getClientIp(httpServletRequest),
                rateLimitProperties.getVerifyEmail().getCapacity(),
                rateLimitProperties.getVerifyEmail().getDuration()
        );
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may attempts to verify email");
        }

        user.setIsVerified(true);
        userRepository.save(user);
        verificationToken.setUsed(true);
        verificationToken.setUsedAt(LocalDateTime.now());
        verificationTokenRepository.save(verificationToken);

        auditLogService.log(user.getId(), AuditAction.USER_VERIFIED_EMAIL.name(),
                AccountUtils.user_entity, user.getId(), AccountUtils.SUCCESS_MESSAGE,
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

            Bucket bucket = rateLimitService.resolveBucket(
                    "VERIFY_EMAIL:" + user.getEmail()+ ":" + httpRequestUtils.getClientIp(httpServletRequest),
                    rateLimitProperties.getResendVerificationEmail().getCapacity(),
                    rateLimitProperties.getResendVerificationEmail().getDuration()
            );
            if (!bucket.tryConsume(1)) {
                throw new TooManyRequestException("Too may attempts requesting email verification");
        }

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
                    AccountUtils.user_entity, user.getId(), AccountUtils.SUCCESS_MESSAGE,
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
                AccountUtils.user_entity, refreshToken.getUserId(), AccountUtils.SUCCESS_MESSAGE,
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
                AccountUtils.user_entity, user.getId(), AccountUtils.SUCCESS_MESSAGE,
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
                AccountUtils.user_entity, user.getId(), AccountUtils.SUCCESS_MESSAGE,
                AccountUtils.password_changed_description, httpServletRequest);
        return "Password changed successfully";
    }

    @Override
    public String forgotPassword(AuthDto.ForgotPasswordRequest request, HttpServletRequest httpServletRequest) {
        UserEntity user = userRepository.findByEmail(request.email()).orElseThrow(() ->
                new ResourceNotFoundException("If an account with that email exists, a password reset link has been sent."));
        Bucket bucket = rateLimitService.resolveBucket(
                "FORGOT_PASSWORD:" + user.getEmail()+ ":" + httpRequestUtils.getClientIp(httpServletRequest),
                rateLimitProperties.getForgotPassword().getCapacity(),
                rateLimitProperties.getForgotPassword().getDuration()
        );
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may attempts on forgot password");
        }

        String token = tokenUtils.generateSecureToken(32);

        passwordResetTokenRepository.save(authMapper.toPasswordResetTokenEntity(user, tokenUtils.hashToken(token)));
        String link = resetPasswordLink + token;

        System.out.printf("forgot password token here %s", token);

        emailService.sendEmailAlert(emailResponseMapper.toResetPassword(user, token));
        auditLogService.log(user.getId(), AuditAction.USER_PASSWORD_RESET_REQUESTED.name(),
                AccountUtils.user_entity, user.getId(), AccountUtils.SUCCESS_MESSAGE,
                AccountUtils.password_reset_requested_description, httpServletRequest);


        return "If an account with that email exists, a password reset link has been sent.";
    }


    @Override
    public String resetPassword(AuthDto.ResetPasswordRequest request, HttpServletRequest httpServletRequest) {
        PasswordResetTokenEntity token = passwordResetTokenRepository.findByToken(tokenUtils.hashToken(request.token())).orElseThrow(() ->
                new BadRequestException("Invalid Token. Unable to reset password"));

        if(token.isUsed()) {
            throw new BadRequestException("Password Reset Failed, Token already used");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("Password Reset Failed, Token has expired");
        }
        if(!request.newPassword().equals(request.confirmPassword())) {
            throw new BadRequestException("Password Reset Failed, Passwords do not match");
        }
        UserEntity user = userRepository.findById(token.getUserId()).orElseThrow(() ->
                new BadRequestException("Password Reset Failed, User not found"));

        Bucket bucket = rateLimitService.resolveBucket(
                "RESET_PASSWORD:" + user.getEmail()+ ":" + httpRequestUtils.getClientIp(httpServletRequest),
                rateLimitProperties.getForgotPassword().getCapacity(),
                rateLimitProperties.getForgotPassword().getDuration()
        );
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestException("Too may reset password attempts");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        token.setUsed(true);
        passwordResetTokenRepository.save(token);
        // revoking all refresh tokens
        refreshTokenRepository.revokeAll(user.getId());
        auditLogService.log(user.getId(), AuditAction.USER_PASSWORD_RESET_COMPLETED.name(),
                AccountUtils.user_entity, user.getId(), AccountUtils.SUCCESS_MESSAGE,
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
            refreshTokenRepository.save(refreshTokenEntity);


            String accessToken = jwtService.generateToken(user, claims);

            return loginResponseMapper.toLoginResponse(accessToken, refreshToken, user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Authentication getAuthentication(AuthDto.LoginRequest request) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(), request.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidResourceException("Invalid email or password");
        } catch (LockedException ex) {
            throw new AccountLockedException(ex.getMessage());
        } catch (DisabledException ex) {
            throw new AccountDisabledException("Your email address has not been verified. \n" +
                    "Please verify your email before logging in.");
//            If an account with this email exists and is not yet verified, a new verification email has been sent.
        } catch (CredentialsExpiredException ex) {
            throw new com.vehiqon.common.exception.CredentialsExpiredException(ex.getMessage());
        }
    }

//    private String getClientIp(HttpServletRequest httpServletRequest) {
//        String xfHeader = httpServletRequest.getHeader("X-Forwarded-For");
//        if(xfHeader == null) {
//            return httpServletRequest.getRemoteAddr();
//        }
//        return xfHeader.split(",")[0];
//    }

}
