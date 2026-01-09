package com.todoapp.service;

import com.todoapp.dto.LoginRequest;
import com.todoapp.dto.SignupRequest;
import com.todoapp.dto.AuthResponse;
import com.todoapp.entity.User;
import com.todoapp.repository.UserRepository;
import com.todoapp.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();

        User savedUser = userRepository.save(user);

        // Create authentication object directly with the saved user
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            savedUser, 
            null, 
            savedUser.getAuthorities()
        );

        String token = jwtProvider.generateToken(authentication);
        return new AuthResponse(token, "Account created successfully");
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtProvider.generateToken(authentication);
        return new AuthResponse(token, "Login successful");
    }
}
