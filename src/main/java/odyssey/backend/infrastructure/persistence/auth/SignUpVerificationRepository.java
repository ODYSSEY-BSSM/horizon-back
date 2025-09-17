package odyssey.backend.infrastructure.persistence.auth;

import odyssey.backend.domain.auth.SignUpVerification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpVerificationRepository extends CrudRepository<SignUpVerification, String> {
}
