package com.example.app_food.Controller;

import com.example.app_food.Email;
import com.example.app_food.Entity.User;
import com.example.app_food.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final Email email;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> signUpPostForm(@RequestBody @Valid User userReq, BindingResult result) {
        Map<String, String> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put("message", "Invalid input data");
            return ResponseEntity.badRequest().body(response);
        }

        if (userService.findByEmail(userReq.getEmail()).isPresent()) {
            response.put("message", "Email already exists");
            return ResponseEntity.badRequest().body(response);
        }

        User user = User.builder()
                .isActive(false)
                .email(userReq.getEmail())
                .password(userReq.getPassword())
                .username(userReq.getUsername())
                .build();

        String randomCode = email.getRandom();
        user.setOtp(randomCode);
        email.sendEmail(user);
        userService.saveUser(user);

        response.put("message", "User registered successfully. Please verify OTP.");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, String>> verifyCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("code");

        Map<String, String> response = new HashMap<>();

        // Kiểm tra user có tồn tại không
        User user = userService.findByEmail(email).orElse(null);
        if (user == null) {
            response.put("message", "invalid_email");
            return ResponseEntity.badRequest().body(response);
        }

        // Kiểm tra mã OTP
        if (code.equals(user.getOtp())) {
            user.setActive(true);
            userService.saveUser(user);
            response.put("message", "success");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "failed");
            return ResponseEntity.badRequest().body(response);
        }
    }



}
