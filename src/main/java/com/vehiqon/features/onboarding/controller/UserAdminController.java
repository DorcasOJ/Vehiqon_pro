package com.vehiqon.features.onboarding.controller;

import com.vehiqon.common.api.dto.response.ApiResponse;
import com.vehiqon.common.enums.EntityEnum;
import com.vehiqon.common.api.mapper.ApiResponseMapper;
import com.vehiqon.features.insights.analytics.enums.EntityIdSource;
import com.vehiqon.features.insights.analytics.enums.EventType;
import com.vehiqon.features.insights.analytics.service.around.AnalyticsAction;
import com.vehiqon.features.insights.auditLog.enums.AuditActionType;
import com.vehiqon.features.insights.auditLog.service.around.AuditAction;
import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto.UserResponse>>> getAllUsers(
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable)
    {
        boolean hasInvalidSort = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equalsIgnoreCase("string"));
        if (hasInvalidSort) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }
        return ResponseEntity.ok(apiResponseMapper.toResponse( userService.getAllUser(pageable)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto.UserResponse>> getUserById(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(apiResponseMapper.toResponse( userService.getUser(userId)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserDto.UserResponse>>> searchUsers(
            @RequestParam String query,
            @Parameter(hidden = true)
            @PageableDefault(page = 0, size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        boolean hasInvalidSort = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equalsIgnoreCase("string"));
        if (hasInvalidSort) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }
        return ResponseEntity.ok(apiResponseMapper.toResponse( userService.searchUser(query, pageable)));
    }


    @AnalyticsAction(
            value = EventType.ROLE_UPDATED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "userId"
    )
    @AuditAction(
            value = AuditActionType.ROLE_UPDATED,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam =  "userId"
    )
    @PatchMapping("/{userId}/roles")
    public ResponseEntity<ApiResponse<String>> updateRoles(
            @PathVariable UUID userId,
            @Valid @RequestBody UserDto.UpdateRolesRequest request, HttpServletRequest httpServletRequest
            ) {
        userService.updateRoles(userId, request, httpServletRequest);
        return ResponseEntity.ok(apiResponseMapper.toResponse("Roles updated successfully"));
    }


    @AnalyticsAction(
            value = EventType.ROLE_SYNCED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "userId"
    )
    @AuditAction(
            value = AuditActionType.ROLE_SYNCED,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam =  "userId"
    )
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


    @AnalyticsAction(
            value = EventType.USER_DELETED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "userId"
    )
    @AuditAction(
            value = AuditActionType.USER_DELETED,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "userId"
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(apiResponseMapper.toResponse(
                "Deleted successfully"
        ));
    }

    @AnalyticsAction(
            value = EventType.USER_RESTORED,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "userId"
    )
    @AuditAction(
            value = AuditActionType.USER_RESTORED,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.PATH_VARIABLE,
            entityIdParam = "userId"
    )
    @PostMapping("/{userId}/restore")
    public ResponseEntity<ApiResponse<String>> restoreUser(
            @PathVariable UUID userId) {
        userService.restoreUser(userId);
        return ResponseEntity.ok(apiResponseMapper.toResponse(
                "Restored Successfully."
        ));
    }


    @AnalyticsAction(
            value = EventType.USER_RESTORED,
            entityIdSource = EntityIdSource.NONE
    )
    @AuditAction(
            value = AuditActionType.USER_RESTORED,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.NONE
    )
    @PostMapping("/restore")
    public ResponseEntity<ApiResponse<String>> restoreAllUsers(
            @RequestBody List<UUID> userIds) {
        userService.restoreMultipleUser(userIds);
        return ResponseEntity.ok(apiResponseMapper.toResponse(
                "Restored Successfully."
        ));
    }

    @AnalyticsAction(
            value = EventType.USER_DELETED,
            entityIdSource = EntityIdSource.NONE
    )
    @AuditAction(
            value = AuditActionType.USER_DELETED,
            entityType = EntityEnum.USER,
            entityIdSource = EntityIdSource.NONE
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteAllUserCar(
            @RequestBody List<UUID> userIds) {
        userService.deleteMultipleUser(userIds);
        return ResponseEntity.ok(apiResponseMapper.toResponse(
                "Deleted Successfully."
        ));
    }
}
