package std.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private Long userId;
    private String username;
    private String token;
}
