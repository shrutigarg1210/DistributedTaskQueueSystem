package DistributedTaskQueueSystem.demo.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import DistributedTaskQueueSystem.demo.Config.JwUtil;
import DistributedTaskQueueSystem.demo.Model.DTO.AuthResponse;
import DistributedTaskQueueSystem.demo.Model.DTO.LoginRequest;
import DistributedTaskQueueSystem.demo.Model.DTO.RegisterRequest;
import DistributedTaskQueueSystem.demo.Model.Entity.User;
import DistributedTaskQueueSystem.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository    userRepository;
    private final JwUtil jwUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("User already exists with email: " + registerRequest.getEmail());
        }

        // Create new user and save to database
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role("USER") // Default role, can be enhanced to allow different roles
                .build();

        userRepository.save(user);
        log.info("User registered with email: {}", user.getEmail());

        // Generate JWT token for the registered user
        String token = jwUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getEmail(), user.getRole());
    }

    public AuthResponse login(LoginRequest loginRequest) {
        // Authenticate user
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.getEmail()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        log.info("User logged in with email: {}", loginRequest.getEmail());

        // Generate JWT token for the authenticated user
        String token = jwUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getEmail(), user.getRole());
    }
}
