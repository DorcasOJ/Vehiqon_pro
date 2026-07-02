package com.vehiqon.security.service;

import com.vehiqon.features.onboarding.repository.UserRepository;
import com.vehiqon.security.model.CustomerUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(CustomerUserDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found")
                        );

    }
}
