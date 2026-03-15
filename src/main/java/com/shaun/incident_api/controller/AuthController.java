package com.shaun.incident_api.controller;

import com.shaun.incident_api.DTO.LoginRequest;
import com.shaun.incident_api.entity.AppUser;
import com.shaun.incident_api.repository.UserRepository;
import com.shaun.incident_api.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );


        String token = jwtUtil.generateToken(request.getUsername());


        return ResponseEntity.ok(token);
        }  catch (BadCredentialsException e) {
            // Username or password incorrect
            return ResponseEntity.status(401).body("Username or password incorrect");

        } catch (Exception e) {
            e.printStackTrace();;
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // 临时注册接口
    @PostMapping("/register-temp")
    public ResponseEntity<?> registerTemp(@RequestBody LoginRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("用户已存在");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);

        return ResponseEntity.ok("用户创建成功: " + request.getUsername());
    }
}
