package odyssey.backend.roadmap.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.dto.RoadmapRequest;
import odyssey.backend.roadmap.dto.RoadmapResponse;
import org.springframework.stereotype.Service;
import odyssey.backend.roadmap.domain.Roadmap;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;

    public List<RoadmapResponse> findAllRoadmaps() {
        return roadmapRepository.findAll().stream()
                .map(RoadmapResponse::new)
                .toList();
    }

    public RoadmapResponse save(RoadmapRequest request) {
        Roadmap roadmap = roadmapRepository.save(Roadmap.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build());

        return new RoadmapResponse(roadmap);
    }

    public void deleteRoadmapById(Long id) {
        roadmapRepository.deleteById(id);
    }

    @Transactional
    public RoadmapResponse update(Long id, RoadmapRequest request) {
        Roadmap roadmap = roadmapRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 로드맵입니다."));

        roadmap.update(request);

        return new RoadmapResponse(roadmap);
    }


}
