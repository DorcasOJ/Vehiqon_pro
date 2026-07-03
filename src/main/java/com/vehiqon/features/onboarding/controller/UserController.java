package com.vehiqon.features.onboarding.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.utils.AccountUtils;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Vehiqon - User Account Car Mannagement APIs")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public String get() {
        return "Hello World";
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ApiResponse.<UserResponse>builder()
                .responseCode(AccountUtils.USER_FOUND_CODE)
                .responseMessage(AccountUtils.USER_FOUND_MESSAGE)
                .data(userMapper.toResponse(user))
                .build();
    }
    }

//    @Operation(
//            summary = "Create new User Account",
//            description = "Creating a new user and assigning an account ID"
//    )
//    @io.swagger.v3.oas.annotations.responses.ApiResponse(
//            responseCode = "201",
//            description = "Http Status 201 CREATED"
//    )
//    @PostMapping
//    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
//
//        return ResponseEntity.ok(userService.createUser(request)) ;
//    }
//}
