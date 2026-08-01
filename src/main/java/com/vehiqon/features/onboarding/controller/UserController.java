package com.vehiqon.features.onboarding.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.service.around.AnalyticsAction;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.service.around.AuditAction;
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


    @AuditAction(
            value = AuditActionType.USER_VIEWS_PROFILE,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.CURRENT_USER
    )
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> getProfile(HttpServletRequest httpServletRequest) {
//        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(userService.getProfile(httpServletRequest))
        );
    }


    @AnalyticsAction(
            value = EventType.PROFILE_UPDATED,
            entityIdSource = EntityIdSource.CURRENT_USER
    )
    @AuditAction(
            value = AuditActionType.USER_PROFILE_UPDATED,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.CURRENT_USER
    )
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> updateProfile(
            @Valid @RequestBody UserDto.UpdateUserRequest request, HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.ok(
                apiResponseMapper.toResponse(userService.updateProfile(request, httpServletRequest))
        );
    }
}

