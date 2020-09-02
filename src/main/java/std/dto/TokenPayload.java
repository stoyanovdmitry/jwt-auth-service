package std.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TokenPayload {
    private Long userId;
    private String username;
    private List<String> roles;
}
