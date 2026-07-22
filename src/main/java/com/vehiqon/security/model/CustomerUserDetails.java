package com.vehiqon.security.model;

import com.vehiqon.common.enums.RoleEnum;
import com.vehiqon.features.onboarding.entity.UserEntity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record CustomerUserDetails(
        UserEntity user,
        UUID sessionId,
        UUID jti,
        Collection<? extends GrantedAuthority> authorities

) implements UserDetails {
    @NonNull
    @Override public String getUsername() { return user.getEmail(); }
    @Override public String getPassword() { return user.getPassword(); }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.isUserVerified());
    }

}
