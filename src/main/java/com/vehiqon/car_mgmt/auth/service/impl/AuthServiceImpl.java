package com.vehiqon.car_mgmt.auth.service.impl;

import com.vehiqon.car_mgmt.auth.dto.CreateUserRequest;
import com.vehiqon.car_mgmt.auth.dto.LoginResponse;
import com.vehiqon.car_mgmt.auth.dto.request.LoginRequest;
import com.vehiqon.car_mgmt.auth.service.AuthService;
import com.vehiqon.car_mgmt.common.dto.response.ApiResponse;
import com.vehiqon.car_mgmt.common.dto.response.EmailDetails;
import com.vehiqon.car_mgmt.common.entity.User;
import com.vehiqon.car_mgmt.common.enums.Role;
import com.vehiqon.car_mgmt.common.enums.UserStatus;
import com.vehiqon.car_mgmt.common.service.EmailService;
import com.vehiqon.car_mgmt.common.utils.AccountUtils;
import com.vehiqon.car_mgmt.user.dto.response.UserResponse;
import com.vehiqon.car_mgmt.user.mapper.UserMapper;
import com.vehiqon.car_mgmt.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public ApiResponse<UserResponse> register(CreateUserRequest request) {

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
        newUser.setStatus(UserStatus.ACTIVE.name());
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

    @Override
    public ApiResponse<LoginResponse> register(LoginRequest request) {
        return null;
    }


}
