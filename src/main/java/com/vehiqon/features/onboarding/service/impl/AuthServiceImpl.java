package com.vehiqon.features.onboarding.service.impl;

import com.vehiqon.common.exception.InvalidCredentialsException;
import com.vehiqon.common.utils.AccountUtils;
import com.vehiqon.features.onboarding.dto.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.LoginResponse;
import com.vehiqon.features.onboarding.dto.request.LoginRequest;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.mapper.LoginResponseMapper;
import com.vehiqon.features.onboarding.mapper.UserMapper;
import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.features.onboarding.service.AuthService;
import com.vehiqon.common.dto.response.ApiResponse;
import com.vehiqon.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final LoginResponseMapper loginResponseMapper;
    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;



    @Override
    public ApiResponse<UserResponse> register(CreateUserRequest request) {
        UserResponse savedUser = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .responseCode(AccountUtils.USER_CREATION_CODE)
                .responseMessage(AccountUtils.USER_CREATION_MESSAGE)
                .data(savedUser)
                .build();

    }

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        Authentication authenticate = getAuthentication(request);
        UserEntity user = (UserEntity) authenticate.getPrincipal();
        Map<String, Object> claims = new HashMap<>();

//        assert userDetail != null;
        claims.put(
                "roles",
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        String token = jwtService.generateToken(user, claims);
        LoginResponse response = loginResponseMapper.toResponse(token, user);

        return  ApiResponse.<LoginResponse>builder()
                .responseCode(AccountUtils.USER_LOGIN_CODE)
                .responseMessage(AccountUtils.USER_LOGIN_MESSAGE)
                .data(response)
                .build();

    }

    private Authentication getAuthentication(LoginRequest request) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(), request.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
//        catch (DisabledException ex) {
//        throw new AccountDisabledException("Your account is disabled");
//    } catch (LockedException ex) {
//        throw new AccountLockedException("Your account is locked");
//    }
    }


}
