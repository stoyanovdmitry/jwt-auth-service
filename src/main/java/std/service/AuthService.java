package std.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import std.dto.AuthRequest;
import std.dto.AuthResponse;
import std.dto.TokenPayload;
import std.repository.TokenRepository;
import std.util.JwtUtil;

import java.util.List;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public AuthService(JwtUtil jwtUtil, TokenRepository tokenRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
    }

    public AuthResponse generateTokens(AuthRequest request) {
        return generateTokens(
                request.getUserId(),
                request.getUsername(),
                request.getRoles()
        );
    }

    public AuthResponse refreshTokens(String refreshToken) {
        var userId = jwtUtil.readUserId(refreshToken);

        if (tokenRepository.isTokenExist(userId, refreshToken)) {
            return generateTokens(
                    userId,
                    jwtUtil.readUsername(refreshToken),
                    jwtUtil.readRoles(refreshToken)
            );
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Can't find refresh token for current user");
        }
    }

    private AuthResponse generateTokens(Long userId, String username, List<String> roles) {
        var response = jwtUtil.generateTokens(userId, username, roles);
        tokenRepository.saveRefresh(userId, response.getRefresh());

        return response;
    }

    public TokenPayload readToken(String token) {
        return new TokenPayload(
                jwtUtil.readUserId(token),
                jwtUtil.readUsername(token),
                jwtUtil.readRoles(token)
        );
    }

}
