package com.integ.task.service.impl;

import com.integ.task.config.JwtUtil;
import com.integ.task.dto.LoginRequestDto;
import com.integ.task.entity.UserRole;
import com.integ.task.exceptions.GlobalException;
import com.integ.task.repository.UserRepository;
import com.integ.task.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public AuthServiceImpl(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public String login(LoginRequestDto request) {
        UserRole user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new GlobalException("Invalid username or password" , 401 , HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new GlobalException("Invalid username or password",  401 , HttpStatus.UNAUTHORIZED);
        }

        return jwtUtil.generateToken(user.getUsername());
    }
}