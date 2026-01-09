package com.todoapp.security;

import com.todoapp.entity.User;
import com.todoapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        
        if (email == null) {
            email = oauth2User.getAttribute("sub"); // Fallback to Google ID
        }
        
        final String userEmail = email; // Make final for lambda
        final String userName = name; // Make final for lambda
        
        // Find or create user
        User user = userRepository.findByEmail(userEmail)
            .orElseGet(() -> {
                // Create new user from OAuth2
                User newUser = User.builder()
                    .email(userEmail)
                    .name(userName != null ? userName : userEmail.split("@")[0]) // Use email prefix if name is null
                    .password("OAUTH_USER") // Placeholder password for OAuth users
                    .build();
                return userRepository.save(newUser);
            });

        // Generate JWT token
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken =
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        
        String token = jwtProvider.generateToken(authToken);
        
        // Log for debugging
        System.out.println("=== OAuth2SuccessHandler ===");
        System.out.println("User authenticated: " + userEmail);
        System.out.println("Token generated: " + (token != null ? "Yes" : "No"));
        System.out.println("Token length: " + (token != null ? token.length() : 0));

        // Redirect to frontend with token
        // Use URL encoding to ensure token is properly included
        String redirectUrl = "http://localhost:5173/auth/callback?token=" + 
            java.net.URLEncoder.encode(token, java.nio.charset.StandardCharsets.UTF_8);
        
        System.out.println("Redirect URL (first 150 chars): " + redirectUrl.substring(0, Math.min(150, redirectUrl.length())));
        System.out.println("============================");

        // Set redirect strategy to always redirect
        setAlwaysUseDefaultTargetUrl(false);
        response.sendRedirect(redirectUrl);
    }
}
