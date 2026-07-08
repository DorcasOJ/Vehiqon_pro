package com.vehiqon.features.wallet.repository;

import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.wallet.entity.VirtualAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VirtualAccountRepository extends JpaRepository<VirtualAccountEntity, UUID> {
    Optional<VirtualAccountEntity> findByUser(UserEntity user);
    Optional<VirtualAccountEntity> findByAccountNumber(String accountNumber);
    boolean existsByUser(UserEntity user);
}
