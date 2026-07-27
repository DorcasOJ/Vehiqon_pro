package com.vehiqon.features.onboarding.controller;

import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.common.mapper.ApiResponseMapper;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name="User Admin")
public class UserAdminController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ApiResponseMapper apiResponseMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> updateRoles(
            @PathVariable UUID userId, HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.ok(apiResponseMapper.toResponse( userService.getUser(userId, httpServletRequest)));
    }

    @PatchMapping("/{userId}/roles")
    public ResponseEntity<ApiResponse<String>> updateRoles(
            @PathVariable UUID userId,
            @Valid @RequestBody UserDto.UpdateRolesRequest request, HttpServletRequest httpServletRequest
            ) {
        userService.updateRoles(userId, request, httpServletRequest);
        return ResponseEntity.ok(apiResponseMapper.toResponse("Roles updated successfully"));
    }

    @PutMapping("/{userId}/roles")
    public ResponseEntity<ApiResponse<String>> syncRoles(
            @PathVariable UUID userId,
            @RequestBody UserDto.SyncRolesRequest request, HttpServletRequest httpServletRequest
            ) {
        userService.syncRoles(userId, request, httpServletRequest);
        return ResponseEntity.ok(apiResponseMapper.toResponse("Roles synced successfully"));
    }

    @PatchMapping("/{userId}/unlock")
    public ResponseEntity<ApiResponse<String>> unlockUser(@PathVariable UUID userId,   HttpServletRequest request) {
        userService.unlockUser(userId, request);
        return ResponseEntity.ok(apiResponseMapper.toResponse(
               "User Unlocked."
        ));
    }
}
