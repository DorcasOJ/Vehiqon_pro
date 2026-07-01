package com.casdore.car_mgmt.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    @Schema(
            name="User Account Name"
    )
    private String name;

    @Schema(
            name="User Account Email"
    )
    private String email;
}
