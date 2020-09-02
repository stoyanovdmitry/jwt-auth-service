package std.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthRequest {
    private Long userId;
    private String username;
    private List<String> roles;
}
