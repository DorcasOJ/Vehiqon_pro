package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.enums.*;
import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceAlreadyExistException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.common.service.AuditLogService;
import com.vehiqon.common.utils.AccountUtils;
import com.vehiqon.common.utils.GenerateOrHashTokenUtils;
import com.vehiqon.features.email.mapper.EmailResponseMapper;
import com.vehiqon.features.email.mapper.VerificationTokenMapper;
import com.vehiqon.features.onboarding.dto.request.UserDto;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.email.service.EmailService;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.repository.VerificationTokenRepository;
import com.vehiqon.features.onboarding.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${VERIFICATION_URL}")
    private String verificationLink;

    private final UserMapper userMapper;
    private final EmailResponseMapper emailResponseMapper;
    private final VerificationTokenMapper verificationTokenMapper;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final GenerateOrHashTokenUtils tokenUtils;
    private final AuditLogService auditLogService;
//    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public UserResponse createUser(UserDto.CreateUserRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new ResourceAlreadyExistException("Account already exist for this Email. Kindly Login.");
        }
        UserEntity newUser = userMapper.toEntity(request);
        newUser.setStatus(UserStatus.ACTIVE.name());
        newUser.setIsVerified(false);
        newUser.setPassword(passwordEncoder.encode(request.password())); // hash password
        if (request.role() == null) {
            newUser.getRoles().add(RoleEnum.ROLE_USER);
        } else {
            newUser.getRoles().add(request.role());
        }
        UserEntity savedUser = userRepository.save(newUser);
        validateUserEmail(savedUser);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public void validateUserEmail(UserEntity savedUser) {
        String token = tokenUtils.generateSecureToken(32);

        verificationTokenRepository.save(verificationTokenMapper.emailTokenToSave(savedUser, tokenUtils.hashToken(token)));
        String url = verificationLink+ token;

        System.out.printf("Email token here: %s", token);

        emailService.sendEmailAlert(emailResponseMapper.toVerifyEmailResponse(savedUser, url));
//        emailService.sendEmailAlert(emailResponseMapper.toAccountCreationResponse(savedUser));
    }

    @Override
    public UserResponse updateProfile(UserDto.UpdateUserRequest request) {
        UserEntity user = getAuthenticatedUser();
        userMapper.updateEntity(request, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getProfile() {
        UserEntity user = getAuthenticatedUser();
//        String salt = org.springframework.security.crypto.keygen.KeyGenerators.string().generateKey();
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void updateRoles(UUID userId, UserDto.UpdateRolesRequest request) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new BadRequestException("Role Update Failed, User not found"));

        if (request.remove() != null) {
            user.removeRoles(request.remove());
        }

        if(request.add() != null) {
            user.addRoles(request.add());
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void syncRoles(UUID userId, UserDto.SyncRolesRequest request) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new BadRequestException("Role Update Failed, User not found"));
        if (request.roles() != null) {
            user.syncRoles(request.roles());
        }
        userRepository.save(user);
    }

    @Override
    public void unlockUser(UUID userId, HttpServletRequest request) {
        UserEntity adminUser = getAuthenticatedUser();
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        user.setLockedUntil(null);
        user.setFailedLoginAttempts(0);
        auditLogService.log(adminUser.getId(), AuditAction.ACCOUNT_UNLOCKED.name(),
                EntityEnum.USER, user.getId(),  AuditStatus.SUCCESS,
                AuditAction.ACCOUNT_UNLOCKED.getDescription(), request );

    }

    private UserEntity getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new BadRequestException(
                    "User is not authenticated");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }


}
