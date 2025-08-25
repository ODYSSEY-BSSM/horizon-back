package odyssey.backend.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.image.domain.Image;
import odyssey.backend.image.service.ImageService;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.websocket.dto.roadmap.RoadmapUpdateMessage;
import odyssey.backend.websocket.dto.roadmap.RoadmapUpdateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketRoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final ImageService imageService;

    @Transactional
    public RoadmapUpdateResponse update(RoadmapUpdateMessage message) {
        Roadmap roadmap = roadmapRepository.findById(message.getRoadmapId())
                .orElseThrow(RoadmapNotFoundException::new);

        roadmap.update(message.getTitle(), message.getDescription(), message.getCategories());
        roadmap.updateLastModifiedAt();

        log.info("로드맵 수정 - id: {}, title: {}, description: {}, categories: {}",
                roadmap.getId(), roadmap.getTitle(), roadmap.getDescription(), roadmap.getCategories());

        roadmapRepository.save(roadmap);

        Image image = imageService.getImageByRoadmap(roadmap);

        return RoadmapUpdateResponse.from(roadmap, image,1L, "아직 고정닉");

    }

}
