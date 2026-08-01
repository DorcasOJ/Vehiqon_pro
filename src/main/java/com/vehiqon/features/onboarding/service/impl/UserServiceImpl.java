package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.enums.RoleEnum;
import com.vehiqon.common.enums.UserStatus;
import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceAlreadyExistException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.common.utils.GenerateOrHashTokenUtils;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.Notification.dto.NotificationDto;
import com.vehiqon.features.insights.Notification.enums.NotificationEvent;
import com.vehiqon.features.insights.analytics.dto.requestScope.AnalyticsContext;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.enums.AuditStatus;
import com.vehiqon.features.insights.enums.PublishAction;
import com.vehiqon.features.insights.auditLog.dto.AuditLogDto;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.mapper.VerificationTokenMapper;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.repository.VerificationTokenRepository;
import com.vehiqon.features.onboarding.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${VERIFICATION_URL}")
    private String verificationLink;

    private final UserMapper userMapper;
    private final VerificationTokenMapper verificationTokenMapper;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenerateOrHashTokenUtils tokenUtils;
    private final AnalyticsContext analyticsContext;
    private final InsightEventPublisher publisher;

    @Override
    @Transactional
    public UserDto.UserResponse createUser(UserDto.CreateUserRequest request) {
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
        log.info("Email token here: {}", token);
        publisher.publish(
                new NotificationDto.VerifyEmail(PublishAction.NOTIFICATION, savedUser.getId(), savedUser.getEmail(), url,
                        NotificationEvent.VERIFY_EMAIL)
        );

    }

    @Override
    public UserDto.UserResponse updateProfile(UserDto.UpdateUserRequest request, HttpServletRequest httpServletRequest) {
        UserEntity user = getAuthenticatedUser();

        userMapper.updateEntity(request, user);
        //        publisher.publish( new AuditLogDto.AuditEvent(user.getId(), AuditActionType.USER_PROFILE_UPDATED, EntityEnum.USER,
//                user.getId(), AuditStatus.SUCCESS, httpServletRequest, PublishAction.AUDIT_LOG));
        UserDto.UserResponse response = userMapper.toResponse(userRepository.save(user));
        Map<String, Object> updatedFields = getUpdatedFields(request);
        analyticsContext.put("fieldCount", updatedFields.size());
        analyticsContext.put("updatedFields", updatedFields);
        analyticsContext.put("updateSource", "profile");
        return response;
    }

    @Override
    public UserDto.UserResponse getProfile(HttpServletRequest httpServletRequest) {
        UserEntity user = getAuthenticatedUser();
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void updateRoles(UUID userId, UserDto.UpdateRolesRequest request,  HttpServletRequest httpServletRequest) {
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
    public void syncRoles(UUID userId, UserDto.SyncRolesRequest request,  HttpServletRequest httpServletRequest) {
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
        if (!user.isLocked()) {
            throw new BadRequestException("User is not locked");
        }
        user.setLockedUntil(null);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
//        publisher.publish( new AuditLogDto.AuditEvent(adminUser.getId(), AuditActionType.USER_UNLOCKED, EntityEnum.USER,
//                user.getId(), AuditStatus.SUCCESS, request, PublishAction.AUDIT_LOG));
    }

    @Override
    public UserDto.UserResponse getUser(UUID userId, HttpServletRequest request) {
        UserEntity adminUser = getAuthenticatedUser();
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
//        publisher.publish( new AuditLogDto.AuditEvent(adminUser.getId(), AuditActionType.GET_USER, EntityEnum.USER,
//                user.getId(), AuditStatus.SUCCESS, request, PublishAction.AUDIT_LOG));
        return userMapper.toResponse(user);
    }

    private UserEntity getAuthenticatedUser() {
//        CustomerUserDetails authenticatedUser = authService.getAuthenticatedUser();
//        return authenticatedUser.user();
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

    private Map<String, Object> getUpdatedFields(Object record) {
        Map<String, Object> updatedFields = new LinkedHashMap<>();
        for(RecordComponent component : record.getClass().getRecordComponents()) {
            try {
                Object value = component.getAccessor().invoke(record);
                if(value != null) {
                    updatedFields.put(component.getName(), value);
                }
            } catch (Exception e) {
                throw new BadRequestException("Failed to read record component." + e.getMessage());
            }
        }
        return updatedFields;
    }


}
