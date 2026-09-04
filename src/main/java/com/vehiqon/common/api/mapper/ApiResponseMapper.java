package com.vehiqon.common.api.mapper;

import com.vehiqon.common.api.dto.response.ApiResponse;
import com.vehiqon.common.utils.AccountUtils;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseMapper {

    public <T> ApiResponse<T> toResponse (T data) {
        return  ApiResponse.<T>builder()
                .success(true)
                .responseCode(AccountUtils.SUCCESS_CODE)
                .responseMessage(AccountUtils.SUCCESS_MESSAGE)
                .data(data)
                .build();
    }
}
