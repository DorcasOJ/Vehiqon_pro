package com.vehiqon.onboarding.service.impl;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.email.EmailDetails;
import com.vehiqon.common.entity.User;
import com.vehiqon.common.enums.Role;
import com.vehiqon.common.enums.UserStatus;
import com.vehiqon.email.service.EmailService;
import com.vehiqon.common.utils.AccountUtils;
import com.vehiqon.onboarding.dto.CreateUserRequest;
import com.vehiqon.onboarding.dto.response.UserResponse;
import com.vehiqon.onboarding.mapper.UserMapper;
import com.vehiqon.onboarding.repository.UserRepository;
import com.vehiqon.onboarding.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    UserMapper userMapper;

    @Autowired
    EmailService emailService;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<UserResponse> createUser(CreateUserRequest request) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if(existingUser.isPresent()) {
            User user = existingUser.get();
            return ApiResponse.<UserResponse>builder()
                    .responseCode(AccountUtils.USER_EXIST_CODE)
                    .responseMessage(AccountUtils.USER_EXIST_MESSAGE)
                    .data(userMapper.toResponse(user))
                    .build();
        }

        User newUser = userMapper.toEntity(request);
        newUser.setStatus(UserStatus.INACTIVE.name());
        newUser.setIsVerified(false);
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // hash password
        newUser.getRoles().add(Role.ROLE_USER);
        User savedUser = userRepository.save(newUser);



        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations your Account have been successfully created.\n" +
                        "Your Primary Account Details:\n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "\n" +
                        "Account NUBAN: 0000000000")
                .build();
        emailService.sendEmailAlert(emailDetails);

        return ApiResponse.<UserResponse>builder()
                .responseCode(AccountUtils.USER_CREATION_CODE)
                .responseMessage(AccountUtils.USER_CREATION_MESSAGE)
                .data(userMapper.toResponse(savedUser))
                .build();

    }
}
