package com.vehiqon.features.onboarding.service.impl;

import com.sun.security.auth.UserPrincipal;
import com.vehiqon.common.enums.VerificationTokenTypeEnum;
import com.vehiqon.common.exception.*;
import com.vehiqon.common.utils.AccountUtils;
import com.vehiqon.features.onboarding.dto.request.*;
import com.vehiqon.features.onboarding.dto.response.LoginResponse;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.RefreshTokenEntity;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.entity.VerificationTokenEntity;
import com.vehiqon.features.onboarding.mapper.*;
import com.vehiqon.features.onboarding.repository.RefreshTokenRepository;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.repository.VerificationTokenRepository;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.features.onboarding.service.UserService;
import com.vehiqon.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final LoginResponseMapper loginResponseMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;



    @Override
    public ApiResponse<UserResponse> register(CreateUserRequest request) {
        UserResponse savedUser = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .responseCode(AccountUtils.USER_CREATION_CODE)
                .responseMessage(AccountUtils.USER_CREATION_MESSAGE)
                .data(savedUser)
                .build();

    }

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest request, HttpServletRequest
            httpRequest) {
        Authentication authenticate = getAuthentication(request);
        UserEntity user = (UserEntity) authenticate.getPrincipal();
        LoginResponse response = generateTokens(user, httpRequest);

        return  ApiResponse.<LoginResponse>builder()
                .success(true)
                .responseCode(AccountUtils.USER_LOGIN_CODE)
                .responseMessage(AccountUtils.USER_LOGIN_MESSAGE)
                .data(response)
                .build();

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


//            System.out.println(refreshTokenEntity.getUser());
//            System.out.println(refreshTokenEntity.getUser().getEmail());
//            RefreshTokenEntity existing =
//                    refreshTokenRepository.findByUser(user).
//                            orElse(null);
//
//            if (existing != null) {
//                String accessToken = jwtService.generateToken(user, claims);
//                String refreshToken = jwtService.generateRefreshToken(user);
//
//                RefreshTokenEntity refreshTokenEntity = jwtService.getRefreshTokenToSave(refreshToken, user, request );
//
////                existing.setToken(newToken);
////                existing.setExpiresAt(expiry);
////                existing.setRevoked(false);
////                existing.setExpired(false);
//
//                refreshTokenRepository.save(existing);
//            } else {
//                refreshTokenRepository.save(refreshTokenEntity);
//            }
            String accessToken = jwtService.generateToken(user, claims);
            String refreshToken = jwtService.generateRefreshToken(user);

            RefreshTokenEntity refreshTokenEntity =
                    jwtService.getRefreshTokenToSave(refreshToken, user, request);

            RefreshTokenEntity existing = refreshTokenRepository
                    .findByUser(user)
                    .orElse(null);

//            if (existing != null) {
//                existing.setToken(refreshToken);
//                existing.setExpiresAt(refreshTokenEntity.getExpiresAt());
//                existing.setDeviceId(refreshTokenEntity.getDeviceId());
//                existing.setDeviceName(refreshTokenEntity.getDeviceName());
//                existing.setIpAddress(refreshTokenEntity.getIpAddress());
//                existing.setExpired(false);
//                existing.setRevoked(false);
//                existing.setRevokedAt(null);
//
//                refreshTokenRepository.save(existing);
//            } else {
//                refreshTokenRepository.save(refreshTokenEntity);
//            }

            return loginResponseMapper.toResponse(accessToken, null, user);
           } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public ApiResponse<LoginResponse> refresh(RefreshTokenRequest request, HttpServletRequest httpRequest) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
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

        UserEntity user = refreshToken.getUser();
        LoginResponse response = generateTokens(user, httpRequest);
        return ApiResponse.<LoginResponse>builder()
                .responseCode(AccountUtils.SUCCESS_CODE)
                .responseMessage(AccountUtils.SUCCESS_MESSAGE)
                .data(response)
                .build();

    }

    private Authentication getAuthentication(LoginRequest request) {
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

    @Override
    @Transactional
    public ApiResponse<Void> verifyEmail(String token) {
        VerificationTokenEntity verificationToken = verificationTokenRepository
                .findByTokenAndType(
                        token,
                        VerificationTokenTypeEnum.EMAIL_VERIFICATION
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid verification token")
                );

        if (Boolean.TRUE.equals(verificationToken.getUsed())) {
            throw new IllegalArgumentException("Verification link has already been used.");
        }

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification link has expired.");
        }
        UserEntity user = verificationToken.getUser();
        user.setIsVerified(true);
        userRepository.save(user);
        verificationToken.setUsed(true);
        verificationToken.setUsedAt(LocalDateTime.now());
        verificationTokenRepository.save(verificationToken);
        return ApiResponse.<Void>builder()
                .responseCode(AccountUtils.SUCCESS_CODE)
                .responseMessage("Email verified successfully.")
                .build();
    }


    @Override
    @Transactional
    public ApiResponse<Void> resendVerificationEmail(
                    ResendVerificationRequest request
    ) {

        Optional<UserEntity> optionalUser =
                    userRepository.findByEmail(request.email());

            if (optionalUser.isEmpty()) {
                return ApiResponse.<Void>builder()
                        .responseCode(AccountUtils.SUCCESS_CODE)
                        .responseMessage(
                                "If an account exists with this email, a verification email has been sent."
                        )
                        .build();
            }

            UserEntity user = optionalUser.get();
            if (Boolean.TRUE.equals(user.getIsVerified())) {
                return ApiResponse.<Void>builder()
                        .responseCode(AccountUtils.SUCCESS_CODE)
                        .responseMessage("Email is already verified.")
                        .build();
            }

            // invalidate previous unused tokens
            List<VerificationTokenEntity> tokens =
                    verificationTokenRepository.findByUserAndTypeAndUsedFalse(
                            user,
                            VerificationTokenTypeEnum.EMAIL_VERIFICATION
                    );
            for (VerificationTokenEntity token : tokens) {
                token.setUsed(true);
                token.setUsedAt(LocalDateTime.now());
            }
            verificationTokenRepository.saveAll(tokens);
            userService.validateUserEmail(user);
            return ApiResponse.<Void>builder()
                    .success(true)
                    .responseCode(AccountUtils.SUCCESS_CODE)
                    .responseMessage(
                            "If an account exists with this email, a verification email has been sent."
                    )
                    .build();
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
    public ApiResponse<Void> logout(LogoutRequest request) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));
        refreshToken.setRevoked(true);
        refreshToken.setExpired(true);
        refreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
        return ApiResponse.<Void>builder()
                .success(true)
                .responseCode(AccountUtils.SUCCESS_CODE)
                .responseMessage("Logged out Successful")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Void> logoutAll() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        UserEntity user =
                (UserEntity) authentication.getPrincipal();

        refreshTokenRepository.revokeAll(user);
        log.info("Revoked refresh tokens for user {}",user.getEmail());

        return ApiResponse.<Void>builder()
                .success(true)
                .responseCode(AccountUtils.SUCCESS_CODE)
                .responseMessage("Logged out from all devices")
                .build();
    }


}
