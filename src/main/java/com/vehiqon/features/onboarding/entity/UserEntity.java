package com.vehiqon.features.onboarding.entity;

import com.vehiqon.features.carmgmt.entities.CarEntity;
import com.vehiqon.common.entity.Notification;
import com.vehiqon.common.entity.AuditLog;
import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.common.entity.UserSubscription;
import com.vehiqon.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="users")
public class UserEntity extends BaseEntity implements UserDetails {
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String primaryAccountNumber;
    private String email;
    private String password;
    private String phoneNumber;
    private String status;
    private Boolean isVerified;


//    @OneToMany(mappedBy = "user")
//    private Set<UserRole> userRole; // for user role
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private  Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<CarEntity> cars;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private Set<AuditLog> auditLog;

    @OneToMany(mappedBy = "user")
    private Set<UserSubscription> userPlan;

    @OneToMany(mappedBy = "user")
    private Set<RefreshTokenEntity> refreshTokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(Role::name)
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
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isVerified;
    }


}
