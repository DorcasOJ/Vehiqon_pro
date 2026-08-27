package com.vehiqon.features.onboarding.mapper;

import com.vehiqon.features.onboarding.dto.UserDto;
import com.vehiqon.features.onboarding.dto.response.UserProfileResponse;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import com.vehiqon.features.onboarding.entity.UserEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    return from creating user
    UserDto.UserResponse toResponse(UserEntity user);

//    return user profile
    @Mapping(target = "fullName",
    expression = """
            java(
                  java.util.stream.Stream
                      .of(user.getFirstName(),
                          user.getLastName())
                      .filter(java.util.Objects::nonNull)
                      .filter(s -> !s.isBlank())
                      .collect(java.util.stream.Collectors.joining(" "))
              )
            """)
    UserProfileResponse toProfile(UserEntity user);

//    get list of all user
    List<UserDto.UserResponse> toResponses(List<UserEntity> users);

    //    create new user
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "lockedUntil", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "lastFailedLoginAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
//    @Mapping(target = "sessionId", ignore = true)
    UserEntity toEntity(UserDto.CreateUserRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "lockedUntil", ignore = true)
    @Mapping(target = "failedLoginAttempts", ignore = true)
    @Mapping(target = "lastFailedLoginAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
//    @Mapping(target = "jti", ignore = true)
//    @Mapping(target = "sessionId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(
            UserDto.UpdateUserRequest request,
            @MappingTarget UserEntity user
    );

}
