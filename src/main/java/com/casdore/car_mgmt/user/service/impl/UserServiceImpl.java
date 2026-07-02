package com.casdore.car_mgmt.user.service.impl;

import com.casdore.car_mgmt.common.dto.response.EmailDetails;
import com.casdore.car_mgmt.auth.dto.CreateUserRequest;
import com.casdore.car_mgmt.common.enums.UserStatus;
import com.casdore.car_mgmt.user.dto.response.UserResponse;
import com.casdore.car_mgmt.common.dto.response.ApiResponse;
import com.casdore.car_mgmt.common.entity.User;
import com.casdore.car_mgmt.user.mapper.UserMapper;
import com.casdore.car_mgmt.user.repository.UserRepository;
import com.casdore.car_mgmt.common.service.EmailService;
import com.casdore.car_mgmt.user.service.UserService;
import com.casdore.car_mgmt.common.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
//        newUser.setPassword(request.getPassword()); // hash password
        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation")
                .messageBody("Congratulations your Account have been successfully created.\n" +
                        "Your Primary Account Details:\n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\n" +
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
