package com.vehiqon.features.onboarding.entity;

import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private  Set<Role> roles = new HashSet<>();
//
//    @OneToOne(mappedBy = "user")
//    private VirtualAccountEntity virtualAccount;
//
//    @Builder.Default
//    @OneToMany(mappedBy = "user")
//    private Set<CarEntity> cars = new HashSet<>();;
//
//    @Builder.Default
//    @OneToMany(mappedBy = "user")
//    private Set<Notification> notifications = new HashSet<>();;
//
//    @Builder.Default
//    @OneToMany(mappedBy = "user")
//    private Set<AuditLog> auditLog = new HashSet<>();;
//
//    @Builder.Default
//    @OneToMany(mappedBy = "user")
//    private Set<UserSubscription> userPlan = new HashSet<>();;
//
//    @Builder.Default
//    @OneToMany(mappedBy = "user")
//    private Set<RefreshTokenEntity> refreshTokens = new HashSet<>();;

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
        return Boolean.TRUE.equals(isVerified);
//        return isVerified;
    }

}
