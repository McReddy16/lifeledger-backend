package com.lifeledger.userdetails;

import com.lifeledger.userdetails.UserRequestDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

 

@SecurityRequirement(name = "bearerAuth")
    @GetMapping("/all")
    public List<UserDetails> getAllUsers() {
        return userService.getAllUsers();
    }
}
