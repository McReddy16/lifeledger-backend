package com.lifeledger.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {

    // JWT token for authentication
    private String token;

    // Logged-in user's email
    private String email;

    // Logged-in user's display name (username)
    private String username;
}
