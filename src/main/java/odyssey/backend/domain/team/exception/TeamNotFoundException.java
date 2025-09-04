package odyssey.backend.domain.team.exception;

import odyssey.backend.domain.team.exception.error.TeamExceptionProperty;
import odyssey.backend.shared.exception.GlobalException;

public class TeamNotFoundException extends GlobalException {
    public TeamNotFoundException() {
        super(TeamExceptionProperty.TEAM_NOT_FOUND);
    }
}
