package com.project.FitLink.service;


import com.project.FitLink.dto.Auth.LoginRequest;
import com.project.FitLink.dto.Auth.RefreshResponse;
import com.project.FitLink.dto.Auth.RegisterRequest;
import com.project.FitLink.dto.Auth.TokenResponse;
import com.project.FitLink.entities.users.UserEntity;
import com.project.FitLink.repository.users.UserRepository;
import com.project.FitLink.utils.Constants;
import com.project.FitLink.utils.enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class authService {

    private final jwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user and generates access and refresh tokens.
     * @param loginRequest The login request containing email and password.
     * @return A TokenResponse containing the access and refresh tokens.
     * @throws BadCredentialsException If authentication fails due to invalid credentials.
     */
    public TokenResponse loginProcess(LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        );
        Authentication auth = authenticationManager.authenticate(authentication);
        if(auth == null || !auth.isAuthenticated()) {
            throw new BadCredentialsException("Authentication Failed, Invalid username or password");
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        String accessToken = jwtService.generateAccessToken();
        String refreshToken = jwtService.generateRefreshToken();

        String userName = jwtService.extractClaims(accessToken).get("userName", String.class);
        String role = jwtService.extractClaims(accessToken).get("authorities", String.class);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userName(userName)
                .role(role)
                .build();
    }

    // Todo review register flow

    public TokenResponse RegisterUser(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userName(request.getUserName())
                .role(request.getRole() != null ? request.getRole() : Roles.USER)
                .build();

        userRepository.save(user);

       LoginRequest login = LoginRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        return loginProcess(login);
    }

    /**
     * Refreshes the access token using a valid refresh token.
     * @param refreshToken The refresh token to use for refreshing the access token.
     * @return A RefreshResponse containing the new access token.
     * @throws RuntimeException If the refresh token is invalid or expired.
     */
    public RefreshResponse refreshToken(String refreshToken) {
        if(!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException("expired token, please login again");
        }
        Claims claims = jwtService.extractClaims(refreshToken);
        long id = claims.get("id", Long.class);
        String email = claims.get("email", String.class);
        String userName = claims.get("userName", String.class);
        String role = claims.get("authorities", String.class);

        if(!userRepository.existsByEmail(email)){
            throw new RuntimeException("User not found, please login again");
        }

        String newAccessToken = Jwts.builder()
                .issuer("FitLink")
                .subject("ACCESS Token")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + Constants.JWT_ACCESS_TOKEN_EXPIRATION))
                .claim("id", id)
                .claim("email", email)
                .claim("userName", userName)
                .claim("authorities", role)
                .signWith(jwtService.getSecretKey())
                .compact();

        return RefreshResponse.builder()
                .newAccessToken(newAccessToken)
                .build();
    }

}

