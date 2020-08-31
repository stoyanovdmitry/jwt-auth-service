package std.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import std.dto.AuthRequest;
import std.dto.AuthResponse;
import std.dto.TokenPayload;
import std.repository.TokenRepository;
import std.util.JwtUtil;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public AuthService(JwtUtil jwtUtil, TokenRepository tokenRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
    }

    public AuthResponse generateTokens(AuthRequest request) {
        return generateTokens(request.getUserId(), request.getUsername());
    }

    public AuthResponse refreshTokens(String refreshToken) {
        var userId = jwtUtil.readUserId(refreshToken);

        if (jwtUtil.isTokenExpired(refreshToken)) {
            forbid("Refresh token is expired");
        } else if (tokenRepository.isTokenExist(userId, refreshToken)) {
            return generateTokens(userId, jwtUtil.readUsername(refreshToken));
        } else {
            forbid("Can't find refresh token for current user");
        }

        return null;
    }

    private AuthResponse generateTokens(Long userId, String username) {
        var response = jwtUtil.generateTokens(userId, username);
        tokenRepository.saveRefresh(userId, response.getRefresh());

        return response;
    }

    public TokenPayload readToken(String token) {
        if (jwtUtil.isTokenExpired(token)) {
            forbid("Token is expired");
        }

        return new TokenPayload(
                jwtUtil.readUserId(token),
                jwtUtil.readUsername(token)
        );
    }

    private void forbid(String message) throws ResponseStatusException {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
    }
}
