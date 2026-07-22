package com.vehiqon.features.onboarding.entity;

import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.common.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="users")
public class UserEntity extends BaseEntity implements UserDetails {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    private String gender;
    private String address;

    private String bvn;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;
    private String status;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private  Set<RoleEnum> roles = new HashSet<>();

    private Integer failedLoginAttempts;

    private Instant lockedUntil;

    private Instant lastFailedLoginAt;

    @Transient
    private UUID sessionId;

    @Transient
    private UUID jti;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(RoleEnum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
       return !isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(isUserVerified());
    }

    public boolean isUserVerified() {return isVerified;}

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(Instant.now());
    }

    public void lock(Duration duration) {
        lockedUntil = Instant.now().plus(duration);
        failedLoginAttempts =0;
    }


    public void incrementFailedLoginAttempt() {
        failedLoginAttempts++;
    }

//    public void addRole(RoleEnum role) {
//        if(role != null) {
//            this.roles.add(role);
//        }
//    }
//
//    public void removeRole(RoleEnum role) {
//        if(role != null) {
//            this.roles.remove(role);
//        }
//    }

    public void addRoles(Collection<RoleEnum> rolesToAdd) {
        if(rolesToAdd != null && !rolesToAdd.isEmpty()) {
            this.roles.addAll(rolesToAdd);
        }
    }

    public void removeRoles(Collection<RoleEnum> rolesToRemove) {
        if(rolesToRemove != null && !rolesToRemove.isEmpty()) {
            this.roles.removeAll(rolesToRemove);
        }
    }

    public void syncRoles(Collection<RoleEnum> targetRoles) {
        this.roles.clear();
        if(targetRoles != null) {
            this.roles.addAll(targetRoles);
        }
    }

}
