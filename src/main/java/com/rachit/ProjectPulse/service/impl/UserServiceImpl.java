package com.rachit.ProjectPulse.service.impl;

import com.rachit.ProjectPulse.config.JwtUtil;
import com.rachit.ProjectPulse.model.dto.LoginRequest;
import com.rachit.ProjectPulse.model.dto.UserDto;
import com.rachit.ProjectPulse.model.dto.UserMapper;
import com.rachit.ProjectPulse.model.entity.User;
import com.rachit.ProjectPulse.repository.UserRepository;
import com.rachit.ProjectPulse.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public UserDto register(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = UserMapper.toEntity(userDto);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User saved = userRepository.save(user);

        return UserMapper.toDto(saved);
    }

    @Override
    public String login(LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Email or Password"));

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}
