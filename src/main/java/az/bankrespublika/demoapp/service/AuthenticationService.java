package az.bankrespublika.demoapp.service;

import az.bankrespublika.demoapp.dao.entity.User;
import az.bankrespublika.demoapp.dao.repository.UserRepository;
import az.bankrespublika.demoapp.exception.NotFoundException;
import az.bankrespublika.demoapp.model.BaseResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    public ResponseEntity<BaseResponse<String>> register(User request) {
        // Find users in DB (существует ли пользователь в БД)
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exists!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordHash(request.getPassword()))
                .fullName(request.getFullName())
                .build();

        userRepository.save(user);
        return BaseResponse.ok("User registered successfully!");
    }

    public ResponseEntity<BaseResponse<Map<String, String>>> login(User request) {
        // Find users in DB (существует ли пользователь в БД)
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found!"));

        // Equals password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password!");
        }

        // Generate token
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        user.setToken(token);
        userRepository.save(user);

        // Add token in JSON - response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return BaseResponse.ok(response);
    }

    public User validateToken(String token) {
        return userRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token!"));
    }

    private String passwordHash(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
