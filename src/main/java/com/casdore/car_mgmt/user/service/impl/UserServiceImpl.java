package com.casdore.car_mgmt.user.service.impl;

import com.casdore.car_mgmt.common.dto.EmailDetails;
import com.casdore.car_mgmt.common.dto.UserRequest;
import com.casdore.car_mgmt.common.dto.response.UserResponse;
import com.casdore.car_mgmt.common.dto.response.ApiResponse;
import com.casdore.car_mgmt.common.entity.User;
import com.casdore.car_mgmt.user.repository.UserRepository;
import com.casdore.car_mgmt.user.service.EmailService;
import com.casdore.car_mgmt.user.service.UserService;
import com.casdore.car_mgmt.common.utils.AccountUtils;
import com.casdore.car_mgmt.common.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public ApiResponse<UserResponse> createUser(UserRequest request) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if(existingUser.isPresent()) {
            User user = existingUser.get();
            return ApiResponse.<UserResponse>builder()
                    .responseCode(AccountUtils.USER_EXIST_CODE)
                    .responseMessage(AccountUtils.USER_EXIST_MESSAGE)
                    .data(UserResponse.builder()
                            .email(user.getEmail())
                            .phoneNumber(user.getPhoneNumber())
                            .name(user.getFirstName()+ " " + user.getLastName()+ " " + user.getOtherName())
                            .build())
                    .build();
        }

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .otherName(request.getOtherName())
                .email(request.getEmail())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .alternativePhoneNumber(request.getAlternativePhoneNumber())
                .status(UserStatus.ACTIVE.name())
                .address(request.getAddress())
                .build();
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
                .data(UserResponse.builder()
                        .email(savedUser.getEmail())
                        .phoneNumber(savedUser.getPhoneNumber())
                        .name(savedUser.getFirstName()+ " " + savedUser.getLastName()+ " " + savedUser.getOtherName())
                        .build())
                .build();

    }
}
