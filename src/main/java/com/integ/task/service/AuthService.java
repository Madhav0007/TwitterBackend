package com.integ.task.service;

import com.integ.task.dto.AuthResponseDto;
import com.integ.task.dto.LoginRequestDto;
import com.integ.task.dto.RefreshTokenRequestDto;
import com.integ.task.dto.RegisterRequestDto;

public interface AuthService {
    AuthResponseDto login(LoginRequestDto request);
    AuthResponseDto refreshToken(RefreshTokenRequestDto request);
    void logout(RefreshTokenRequestDto request);
    AuthResponseDto register(RegisterRequestDto request);
}