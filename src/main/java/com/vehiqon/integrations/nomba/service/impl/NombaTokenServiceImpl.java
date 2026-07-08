package com.vehiqon.integrations.nomba.service.impl;

import com.vehiqon.features.wallet.mapper.NombaMapper;
import com.vehiqon.integrations.nomba.dto.NombaDto;
import com.vehiqon.integrations.nomba.entity.NombaTokenEntity;
import com.vehiqon.integrations.nomba.repository.NombaTokenRepository;
import com.vehiqon.integrations.nomba.service.NombaAuthService;
import com.vehiqon.integrations.nomba.service.NombaTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NombaTokenServiceImpl implements NombaTokenService {
    private static final Long TOKEN_ID = 1L;
    private final NombaTokenRepository nombaTokenRepository;
    private final NombaAuthService nombaAuthService;
    private final NombaMapper nombaMapper;

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
        return nombaToken.getAccessToken();
    }

    private String refresh(NombaTokenEntity nombaToken) {
        try {
            NombaDto.NombaTokenResponse nombaTokenResponse = nombaAuthService.refreshToken(nombaToken.getRefreshToken());
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
        NombaTokenEntity tokenEntity = nombaMapper.toTokenEntity(nombaTokenResponse.data());
        tokenEntity.setId(TOKEN_ID);
        nombaTokenRepository.save(tokenEntity);
    }

    private boolean isExpiringSoon(NombaTokenEntity entity) {
        return Instant.now().isAfter(entity.getExpiresAt().minus(Duration.ofMinutes(5)));
    }
}
