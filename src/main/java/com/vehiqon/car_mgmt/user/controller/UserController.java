package com.vehiqon.car_mgmt.user.controller;

import com.vehiqon.car_mgmt.auth.dto.CreateUserRequest;
import com.vehiqon.car_mgmt.common.dto.response.ApiResponse;
import com.vehiqon.car_mgmt.user.dto.response.UserResponse;
import com.vehiqon.car_mgmt.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Vehiqon - User Account Car Mannagement APIs")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public String get() {
        return "Hello World";
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
}
