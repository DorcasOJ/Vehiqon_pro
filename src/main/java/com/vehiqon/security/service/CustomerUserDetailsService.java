package com.vehiqon.security.service;

import com.vehiqon.features.onboarding.entity.UserEntity;
import com.vehiqon.features.onboarding.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

//        System.out.println("isAccountNonExpired = " + user.isAccountNonExpired());
//        System.out.println("isAccountNonLocked = " + user.isAccountNonLocked());
//        System.out.println("isCredentialsNonExpired = " + user.isCredentialsNonExpired());
//        System.out.println("isEnabled = " + user.isEnabled());

        return user;

    }
}
