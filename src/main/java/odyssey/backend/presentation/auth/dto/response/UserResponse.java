package odyssey.backend.presentation.auth.dto.response;

import odyssey.backend.domain.team.Team;
import odyssey.backend.domain.auth.User;

import java.util.List;

public record UserResponse(
        String username,
        String email,
        String role,
        List<String> teams
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString(),
                user.getTeams()
                        .stream()
                        .map(Team::getName)
                        .toList()
        );
    }
}
