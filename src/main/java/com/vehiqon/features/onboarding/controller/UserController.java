package com.vehiqon.features.onboarding.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> getProfile(HttpServletRequest httpServletRequest) {
//        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(userService.getProfile(httpServletRequest))
        );

    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> updateProfile(
            @Valid @RequestBody UserDto.UpdateUserRequest request, HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(userService.updateProfile(request, httpServletRequest))
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
