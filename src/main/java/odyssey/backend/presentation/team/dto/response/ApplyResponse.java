package odyssey.backend.presentation.team.dto.response;

import odyssey.backend.domain.team.TeamApply;

public record ApplyResponse(
        Long id,
        String teamName,
        TeamApply.Status applyStatus
) {
    public static ApplyResponse of(TeamApply teamApply) {
        return new ApplyResponse(
                teamApply.getId(),
                teamApply.getTeam().getName(),
                teamApply.getStatus()
        );
    }
}
