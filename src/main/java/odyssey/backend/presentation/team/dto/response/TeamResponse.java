package odyssey.backend.presentation.team.dto.response;

import odyssey.backend.domain.team.Team;
import odyssey.backend.domain.auth.User;

import java.util.List;

public record TeamResponse(
        Long id,
        String name,
        String leader,
        List<String> members
) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getLeaderUsername(),
                team.getMembers().stream()
                        .map(User::getUsername)
                        .toList());
    }
}
