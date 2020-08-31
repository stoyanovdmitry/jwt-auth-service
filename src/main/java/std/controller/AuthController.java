package std.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import std.dto.AuthRequest;
import std.dto.AuthResponse;
import std.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/generate")
    public ResponseEntity<AuthResponse> generateTokens(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.generateTokens(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshTokens(String refreshToken) {
        return ResponseEntity.ok(authService.refreshTokens(refreshToken));
    }
}
