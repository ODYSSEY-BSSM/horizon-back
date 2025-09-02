package odyssey.backend.infrastructure.persistence.team;

import odyssey.backend.domain.team.TeamApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamApplyRepository extends JpaRepository<TeamApply, Long> {
}
