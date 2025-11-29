package com.rachit.ProjectPulse.service;

import com.rachit.ProjectPulse.model.dto.LoginRequest;
import com.rachit.ProjectPulse.model.dto.UserDto;

public interface UserService {
    UserDto register(UserDto userDto);
    String login(LoginRequest request);

}
