package com.vehiqon.features.onboarding.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.common.utils.AccountUtils;
import com.vehiqon.features.onboarding.dto.request.UpdateUserRequest;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Profile")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ApiResponseMapper apiResponseMapper;

//    @GetMapping
//    public String get() {
//        return "Hello World";
//    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
//        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(userService.getProfile())
        );

    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(userService.updateProfile(request))
        );
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
