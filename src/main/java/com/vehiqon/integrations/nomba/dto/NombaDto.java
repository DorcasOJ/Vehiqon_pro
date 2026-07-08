package com.vehiqon.integrations.nomba.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public class NombaDto {

    private NombaDto() {
    }


    public record NombaTokenRequest(
            String grant_type,
            String client_id,
            String client_secret
    ) {
    }

    public record NombaTokenResponse(
            String code,
            String description,
            TokenResponseData data
//            String access_token,
//            String token_type,
//            Long expires_in
    ) {

        public record TokenResponseData(
                String businessId,
                @JsonProperty("access_token")
                String accessToken,
                @JsonProperty("refresh_token")
                String refreshToken,
                Instant expiresAt
        ) {
        }
    }


    public record CreateVirtualAccountRequest(
            String accountRef,
            String accountName,
            String currency, //NGN
            String bvn, //optional
            BigDecimal expectedAmount, //optional
            String expiryDate // omit to make account permanent
    ) {
    }

    public record VirtualAccountResponse(
            String code,
            String description,
            Data data) {
        public record Data(
                String accountHolderId,
                String accountRef,
                String accountName,
                String bankName,
                String bankAccountNumber,
                String bankAccountName,
                String currency,
                Boolean active
        ) {

        }

        public record RefreshTokenRequest(
                String refresh_token
        ) {
        }

        public record RefreshTokenResponse(
                String code,
                String description,
                Data data
        ) {

            public record Data(
                    String access_token,
                    String refresh_token,
                    Instant expiresAt
            ) {
            }
        }
    }
}
