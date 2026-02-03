package com.lifeledger.authentication;

import com.lifeledger.exception.ConflictException;
import com.lifeledger.exception.ResourceNotFoundException;
import com.lifeledger.security.JwtTokenProvider;
import com.lifeledger.userdetails.UserDetails;
import com.lifeledger.userdetails.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    // ======================
    // LOGIN
    // ======================
    public LoginResponseDTO login(LoginRequestDTO dto) {

        // Normalize email (case-insensitive login)
        String email = dto.getEmail().toLowerCase().trim();

        UserDetails user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid credentials")
                );

        // Password validation
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        // Generate JWT
        String token = tokenProvider.generateToken(email);

        // RETURN FULL LOGIN RESPONSE
        return new LoginResponseDTO(
                token,
                user.getEmail(),
                user.getName()   // ✅ THIS IS THE FIX
        );
    }

    // ======================
    // SIGNUP
    // ======================
    public SignupResponseDTO signup(SignupRequestDTO dto) {

        // 🔥 Normalize email (CASE-INSENSITIVE SIGNUP)
        String email = dto.getEmail().toLowerCase().trim();

        // 🔐 Duplicate email check
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists");
        }

        UserDetails user = new UserDetails();
        user.setName(dto.getName());
        user.setEmail(email); // ✅ always lowercase in DB
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        return new SignupResponseDTO(
                "Signup successful",
                email
                
        );
    }
}
