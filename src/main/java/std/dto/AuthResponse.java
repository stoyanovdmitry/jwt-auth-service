package std.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class AuthResponse {
    private final String access;
    private final String refresh;
    private boolean authenticated = true;

    public static AuthResponse of(boolean authenticated) {
        var response = AuthResponse.of(null, null);
        response.setAuthenticated(false);

        return response;
    }
}
