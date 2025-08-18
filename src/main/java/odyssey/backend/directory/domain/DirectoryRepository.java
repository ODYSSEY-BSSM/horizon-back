package odyssey.backend.directory.domain;

import odyssey.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {
    List<Directory> findByParentIsNullAndUser(User user);
}
