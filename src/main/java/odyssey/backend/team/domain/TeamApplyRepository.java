package odyssey.backend.team.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamApplyRepository extends JpaRepository<TeamApply, Long> {
}
