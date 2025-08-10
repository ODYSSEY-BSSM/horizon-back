package odyssey.backend.user.dto.response;

import odyssey.backend.user.domain.Role;
import odyssey.backend.user.domain.User;

public record SignUpResponse(
        Long uuid,
        String email,
        String username,
        Role role
) {
    public static SignUpResponse from(User user) {
        return new SignUpResponse(user.getUuid(), user.getEmail(), user.getUsername(), user.getRole());
    }
}
