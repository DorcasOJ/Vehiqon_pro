package com.vehiqon.integrations.nomba.service.impl;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.common.service.TokenEncryptionService;
import com.vehiqon.common.utils.GenerateOrHashTokenUtils;
import com.vehiqon.features.wallet.mapper.NombaMapper;
import com.vehiqon.integrations.nomba.dto.NombaDto;
import com.vehiqon.integrations.nomba.entity.NombaTokenEntity;
import com.vehiqon.integrations.nomba.repository.NombaTokenRepository;
import com.vehiqon.integrations.nomba.service.NombaAuthService;
import com.vehiqon.integrations.nomba.service.NombaTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NombaTokenServiceImpl implements NombaTokenService {
    private static final Long TOKEN_ID = 1L;
    private final NombaTokenRepository nombaTokenRepository;
    private final NombaAuthService nombaAuthService;
    private final NombaMapper nombaMapper;
    private final GenerateOrHashTokenUtils tokenGenerator;
    private final TokenEncryptionService tokenEncryptionService;

    @Override
    @Transactional
    public String getValidAccessToken() {
        Optional<NombaTokenEntity> tokenOpt = nombaTokenRepository.findById(TOKEN_ID);
        if(tokenOpt.isEmpty()) {
            return authenticate();
        }
        NombaTokenEntity nombaToken = tokenOpt.get();
        if(isExpiringSoon(nombaToken)){
            return refresh(nombaToken);
        }
        return tokenEncryptionService.decryptToken(nombaToken.getAccessToken());
    }

    private String refresh(NombaTokenEntity nombaToken) {
        try {
            NombaDto.NombaTokenResponse nombaTokenResponse = nombaAuthService.refreshToken(tokenEncryptionService.decryptToken( nombaToken.getRefreshToken()));
            saveNombaToken(nombaTokenResponse);
            return nombaTokenResponse.data().accessToken();
        } catch (Exception ex) {
            return authenticate();
        }
    }

    private String authenticate() {
        NombaDto.NombaTokenResponse nombaTokenResponse = nombaAuthService.issueToken();
        saveNombaToken(nombaTokenResponse);
        return nombaTokenResponse.data().accessToken();
    }

    private void saveNombaToken(NombaDto.NombaTokenResponse nombaTokenResponse) {
        NombaTokenEntity tokenEntity = NombaTokenEntity.builder()
                .accessToken(tokenEncryptionService.encryptToken(nombaTokenResponse.data().accessToken()))
                .refreshToken(tokenEncryptionService.encryptToken(nombaTokenResponse.data().refreshToken()))
                .businessId(tokenEncryptionService.encryptToken(nombaTokenResponse.data().businessId()))
                .expiresAt(nombaTokenResponse.data().expiresAt())
                .id(TOKEN_ID)
                .build();
        nombaTokenRepository.save(tokenEntity);
    }

    private boolean isExpiringSoon(NombaTokenEntity entity) {
        return Instant.now().isAfter(entity.getExpiresAt().minus(Duration.ofMinutes(5)));
    }


    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
