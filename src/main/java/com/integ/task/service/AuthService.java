package com.integ.task.service;

import com.integ.task.dto.LoginRequestDto;

public interface AuthService {
    String login(LoginRequestDto request);
}