package com.project.FitLink.auth;


import com.project.FitLink.entities.users.UserEntity;
import com.project.FitLink.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("User not found for this email : " + email));
        String roleName = "ROLE_" + user.getRole().name();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));
        return FitLinkUserDetails.builder()
                .id(user.getId())
                .username(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
