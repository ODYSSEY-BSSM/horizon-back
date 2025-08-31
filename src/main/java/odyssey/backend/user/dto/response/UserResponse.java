package odyssey.backend.user.dto.response;

import odyssey.backend.team.domain.Team;
import odyssey.backend.user.domain.User;

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
