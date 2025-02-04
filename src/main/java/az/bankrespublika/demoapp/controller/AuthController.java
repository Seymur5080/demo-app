package az.bankrespublika.demoapp.controller;

import az.bankrespublika.demoapp.dao.entity.User;
import az.bankrespublika.demoapp.model.BaseResponse;
import az.bankrespublika.demoapp.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> register(@RequestBody User user) {
        return authenticationService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Map<String, String>>> login(@RequestBody User user) {
        return authenticationService.login(user);
    }
}