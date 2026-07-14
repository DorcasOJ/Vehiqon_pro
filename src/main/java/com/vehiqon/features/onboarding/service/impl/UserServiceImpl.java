package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.exception.ResourceAlreadyExistException;
import com.vehiqon.common.exception.ResourceNotFoundException;
import com.vehiqon.features.email.mapper.EmailResponseMapper;
import com.vehiqon.features.email.mapper.VerificationTokenMapper;
import com.vehiqon.features.onboarding.dto.request.UpdateUserRequest;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.common.enums.Role;
import com.vehiqon.common.enums.UserStatus;
import com.vehiqon.features.email.service.EmailService;
import com.vehiqon.features.onboarding.dto.request.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.VerificationTokenEntity;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.repository.VerificationTokenRepository;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.features.onboarding.service.UserService;
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
//    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
         throw new ResourceAlreadyExistException("Account already exist for this Email. Kindly Login.");
        }

        UserEntity newUser = userMapper.toEntity(request);
        newUser.setStatus(UserStatus.ACTIVE.name());
        newUser.setIsVerified(false);
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // hash password
        newUser.getRoles().add(Role.ROLE_USER);
        UserEntity savedUser = userRepository.save(newUser);

        validateUserEmail(savedUser);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public void validateUserEmail(UserEntity savedUser) {
        String token = UUID.randomUUID().toString();
        verificationTokenRepository.save(verificationTokenMapper.emailTokenToSave(savedUser, token));
        String url = verificationLink+ token;
        emailService.sendEmailAlert(emailResponseMapper.toVerifyEmailResponse(savedUser, url));
//        emailService.sendEmailAlert(emailResponseMapper.toAccountCreationResponse(savedUser));
    }

    @Override
    public UserResponse updateProfile(UpdateUserRequest request) {
        UserEntity user = getAuthenticatedUser();
        userMapper.updateEntity(request, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getProfile() {
        UserEntity user = getAuthenticatedUser();
        return userMapper.toResponse(user);
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


//    @Override
//    @Transactional
//    public UserResponse updateProfile(UpdateUserRequest request) {
//        UserEntity user = authService.getAuthenticatedUser();
//        if (request.getPhoneNumber() != null
//                && !request.getPhoneNumber().equals(user.getPhoneNumber())
//                && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
//            throw new BadRequestException("Phone number already exists");
//        }
//            userMapper.updateEntity(request, user);
//
//            return userMapper.toResponse(
//                    userRepository.save(user)
//            );
//
//    }

}
