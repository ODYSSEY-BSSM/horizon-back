package odyssey.backend.infrastructure.persistence.directory;

import odyssey.backend.domain.directory.Directory;
import odyssey.backend.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {
    List<Directory> findByParentIsNullAndUser(User user);
}
