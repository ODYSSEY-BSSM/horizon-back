package odyssey.backend.team.dto.response;

import odyssey.backend.team.domain.Team;
import odyssey.backend.user.domain.User;

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
