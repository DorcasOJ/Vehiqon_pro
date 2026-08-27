package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.enums.RoleEnum;
import com.vehiqon.common.enums.UserStatus;
import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceAlreadyExistException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.common.utils.GenerateOrHashTokenUtils;
import com.vehiqon.features.carmgmt.dto.response.CarDetailsResponse;
import com.vehiqon.features.carmgmt.enums.CarStatus;
import com.vehiqon.features.insights.InsightEventPublisher;
import com.vehiqon.features.insights.Notification.dto.NotificationDto;
import com.vehiqon.features.insights.Notification.enums.NotificationEvent;
import com.vehiqon.features.insights.analytics.dto.requestScope.AnalyticsContext;
import com.vehiqon.features.insights.auditLog.dto.requestScope.AuditContext;
import com.vehiqon.features.insights.enums.PublishAction;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.RecordComponent;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
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
    private final AuditContext auditContext;
    private final InsightEventPublisher publisher;

    @Override
    @Transactional
    public UserDto.UserResponse createUser(UserDto.CreateUserRequest request) {
        if(userRepository.existsByEmailAndDeletedFalse(request.email())){
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

        UserDto.UserResponse response = userMapper.toResponse(userRepository.save(user));
        Map<String, Object> updatedFields = getUpdatedFields(request);
        analyticsContext.recordUpdate("profile", updatedFields);
        auditContext.recordContactChange(updatedFields,user);
        return response;
    }

    @Override
    public UserDto.UserResponse getProfile() {
        UserEntity user = getAuthenticatedUser();
        return userMapper.toResponse(user);
    }

//    ADMIN
    @Override
    public Page<UserDto.UserResponse> getAllUser(Pageable pageable) {
        Page<UserEntity> allUsers = userRepository.findAll(pageable);
        return allUsers.map(userMapper::toResponse);
    }

    @Override
    public Page<UserDto.UserResponse> searchUser(String query, Pageable pageable) {
        Page<UserEntity> usersFound = userRepository.searchUsersForAdmin(query, pageable)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No users found")
                );
        return usersFound.map(user -> new UserDto.UserResponse(
                user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPhoneNumber(), user.getRoles(), user.getStatus(), user.getGender()
        ));
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
        Map<String, Object> updatedFields = getUpdatedFields(request);
        analyticsContext.recordUpdate("user_roles", updatedFields);
        auditContext.recordChange(updatedFields,user);
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
        Map<String, Object> updatedFields = getUpdatedFields(request);
        auditContext.recordChange(updatedFields,user);
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
   }

    @Override
    public UserDto.UserResponse getUser(UUID userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
      return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        UUID adminId = getAuthenticatedUser().getId();
        UserEntity user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        user.softDelete(adminId);
        userRepository.save(user);
        auditContext.recordDelete(userId, adminId, "users");
    }

    @Override
    @Transactional
    public void restoreUser(UUID userId) {
        UserEntity user = userRepository.findByIdAndDeletedTrue(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        user.restore();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteMultipleUser(List<UUID> userIds) {
        LocalDateTime now = LocalDateTime.now();
        UUID adminId = getAuthenticatedUser().getId();
        List<UUID> distinctIds = userIds.stream().distinct().toList();

        if(!doAllUsersExists(distinctIds)) {
            List<UUID> nonExistingIds = findUserIdsThatDoNotExist(distinctIds);
            throw new ResourceNotFoundException("Failed. User Id(s) do not exist." + nonExistingIds);
        }
        int deletedCount = userRepository.softDeleteAllByIdIn(distinctIds, now, adminId);
        auditContext.recordMultipleDelete(distinctIds, adminId, deletedCount, "users");
        if (deletedCount != distinctIds.size()) {
            throw new BadRequestException("Something went wrong. All users were not deleted.");
        }
    }

    @Override
    @Transactional
    public void restoreMultipleUser(List<UUID> userIds) {
        UUID adminId = getAuthenticatedUser().getId();
        List<UUID> distinctIds = userIds.stream().distinct().toList();
        int restoredCount = userRepository.restoreAllByIdIn(distinctIds);
        auditContext.recordMultipleRestored(distinctIds, restoredCount, "users");
        if (restoredCount != distinctIds.size()) {
            throw new BadRequestException("Something went wrong. All users were not deleted.");
        }
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
        return userRepository.findByEmailAndDeletedFalse(userDetails.getUsername())
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


    private boolean doAllUsersExists(List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) return true;
        List<UUID> distinctIds = userIds.stream().distinct().toList();
        long existingCount = userRepository.countByIdInAndDeletedFalse(distinctIds);
        return existingCount == distinctIds.size();
    }

    private List<UUID> findUserIdsThatDoNotExist(List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) return List.of();
        List<UUID> existingIds =userRepository.findExistingIdsByIdIn(userIds);
        return userIds.stream()
                .filter( id -> !existingIds.contains(id))
                .toList();
    }


}
