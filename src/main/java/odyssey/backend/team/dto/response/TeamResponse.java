package odyssey.backend.team.dto.response;

import odyssey.backend.team.domain.Team;

public record TeamResponse(
        Long id,
        String name,
        String leader
) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getLeader());
    }
}
