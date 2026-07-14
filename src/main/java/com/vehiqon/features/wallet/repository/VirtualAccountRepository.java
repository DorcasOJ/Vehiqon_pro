package com.vehiqon.features.wallet.repository;

import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.wallet.entity.VirtualAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VirtualAccountRepository extends JpaRepository<VirtualAccountEntity, UUID> {
//    Optional<VirtualAccountEntity> findByUser(UserEntity user);
//    boolean existsByUser(UserEntity user);

    Optional<VirtualAccountEntity> findByUserId(UUID userId);

    Optional<VirtualAccountEntity> findByAccountNumber(String accountNumber);

    Optional<VirtualAccountEntity> findByAccountReference(String accountReference);

    boolean existsByUserId(UUID userId);

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByAccountReference(String accountReference);

    List<VirtualAccountEntity> findAllByExpiredFalse();
}
