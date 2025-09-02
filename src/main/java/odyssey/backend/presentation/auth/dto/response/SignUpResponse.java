package odyssey.backend.presentation.auth.dto.response;

import odyssey.backend.domain.auth.Role;
import odyssey.backend.domain.auth.User;

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
