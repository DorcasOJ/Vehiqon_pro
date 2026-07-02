package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.features.email.EmailDetails;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.common.enums.Role;
import com.vehiqon.common.enums.UserStatus;
import com.vehiqon.features.email.service.EmailService;
import com.vehiqon.common.utils.AccountUtils;
import com.vehiqon.features.onboarding.dto.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
         throw new IllegalArgumentException("Email already exist");
        }

        UserEntity newUser = userMapper.toEntity(request);
        newUser.setStatus(UserStatus.INACTIVE.name());
        newUser.setIsVerified(false);
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // hash password
        newUser.getRoles().add(Role.ROLE_USER);
        UserEntity savedUser = userRepository.save(newUser);



        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations your Account have been successfully created.\n" +
                        "Your Primary Account Details:\n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "\n" +
                        "Account NUBAN: 0000000000")
                .build();
        emailService.sendEmailAlert(emailDetails);

        return userMapper.toResponse(savedUser);
    }
}
