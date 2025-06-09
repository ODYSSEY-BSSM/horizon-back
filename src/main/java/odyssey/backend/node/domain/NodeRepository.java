package odyssey.backend.node.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {
    Optional<List<Node>> findByRoadmapId(Long roadmapId);

    Optional<Node> findByParentId(Long nodeId);

    @EntityGraph(attributePaths = {"children", "parent"})
    Optional<Node> findByIdAndRoadmapId(Long id, Long roadmapId);

    void deleteByRoadmapId(Long roadmapId);
}

