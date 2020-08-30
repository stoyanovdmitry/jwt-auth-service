package std.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class TokenRepository {

    private final Map<Long, String> userIdToRefreshToken = new HashMap<>();

    public void saveRefresh(Long userId, String refreshToken) {
        userIdToRefreshToken.put(userId, refreshToken);
    }

    public boolean isTokenExist(Long userId, String refreshToken) {
        return Optional.ofNullable(userIdToRefreshToken.get(userId))
                       .map(refreshToken::equals)
                       .orElse(false);
    }
}
