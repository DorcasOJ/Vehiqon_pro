package com.vehiqon.features.onboarding.repository;

import com.vehiqon.common.enums.VerificationTokenTypeEnum;
import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.entity.VerificationTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
//public interface UserRepository extends JpaRepository<UserEntity, UUID> {
//    boolean existsByEmail(String email);
//    boolean existsByPhoneNumber(String phoneNumber);
//    Optional<UserEntity> findByEmail(String email);
//    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
//
//
//}

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByBvn(String bvn);

    @EntityGraph(attributePaths = "roles")
//    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findByBvn(String bvn);

    List<UserEntity> findAllByStatus(String status);

    List<UserEntity> findAllByIsVerified(Boolean isVerified);

    @Modifying
    @Transactional
    @Query("""
            UPDATE UserEntity u
            SET u.isVerified = true
            WHERE u.id = :userId
                AND u.isVerified = false
            """)
    void markUserAsIsVerified(UUID userId);

}
