package com.vehiqon.car_mgmt.user.service.impl;

import com.vehiqon.car_mgmt.common.dto.response.EmailDetails;
import com.vehiqon.car_mgmt.auth.dto.CreateUserRequest;
import com.vehiqon.car_mgmt.common.enums.Role;
import com.vehiqon.car_mgmt.common.enums.UserStatus;
import com.vehiqon.car_mgmt.user.dto.response.UserResponse;
import com.vehiqon.car_mgmt.common.dto.response.ApiResponse;
import com.vehiqon.car_mgmt.common.entity.User;
import com.vehiqon.car_mgmt.user.mapper.UserMapper;
import com.vehiqon.car_mgmt.user.repository.UserRepository;
import com.vehiqon.car_mgmt.common.service.EmailService;
import com.vehiqon.car_mgmt.user.service.UserService;
import com.vehiqon.car_mgmt.common.utils.AccountUtils;
import jakarta.transaction.Transactional;
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
    @Transactional
    public User createUser(CreateUserRequest request) {

           if(userRepository.existsByEmail(request.getEmail())) {
          throw new RuntimeException()
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
