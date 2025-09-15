package odyssey.backend.infrastructure.persistence.problem;

import odyssey.backend.domain.problem.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
