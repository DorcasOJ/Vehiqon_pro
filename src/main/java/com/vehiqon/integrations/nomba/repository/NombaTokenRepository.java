package com.vehiqon.integrations.nomba.repository;

import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.wallet.entity.VirtualAccountEntity;
import com.vehiqon.integrations.nomba.entity.NombaTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface NombaTokenRepository extends JpaRepository<NombaTokenEntity, Long> {

    Optional<NombaTokenEntity> findTopByOrderByExpiresAtDesc();
    boolean existsByBusinessId(String businessId);
}
