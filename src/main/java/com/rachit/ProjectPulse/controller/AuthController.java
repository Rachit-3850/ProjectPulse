package com.rachit.ProjectPulse.controller;

import com.rachit.ProjectPulse.model.dto.JwtResponse;
import com.rachit.ProjectPulse.model.dto.LoginRequest;
import com.rachit.ProjectPulse.model.dto.UserDto;
import com.rachit.ProjectPulse.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        UserDto created = userService.register(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(new JwtResponse(token, request.getEmail(), "USER"));
    }

}
