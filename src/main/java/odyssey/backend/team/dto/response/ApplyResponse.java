package odyssey.backend.team.dto.response;

import odyssey.backend.team.domain.TeamApply;

public record ApplyResponse(
        Long id,
        String teamName,
        boolean approved
) {
    public static ApplyResponse of(TeamApply teamApply) {
        return new ApplyResponse(
                teamApply.getId(),
                teamApply.getTeam().getName(),
                teamApply.isApproved()
        );
    }
}
