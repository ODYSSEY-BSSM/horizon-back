package odyssey.backend.websocket.service;

import lombok.RequiredArgsConstructor;
import odyssey.backend.image.domain.Image;
import odyssey.backend.image.service.ImageService;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.user.Repository.UserRepository;
import odyssey.backend.websocket.dto.RoadmapCrudMessage;
import odyssey.backend.websocket.exception.InvalidActionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WebSocketRoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;

    @Transactional
    public RoadmapCrudMessage handleCrud(Long roadmapId, RoadmapCrudMessage message) {
        switch (message.getAction()) {
            case UPDATE -> {
                Roadmap roadmap = roadmapRepository.findById(roadmapId)
                        .orElseThrow(RoadmapNotFoundException::new);

                roadmap.update(message.getTitle(), message.getDescription(), message.getCategories());

                roadmap.updateLastModifiedAt();

                roadmapRepository.save(roadmap);

                Image image = imageService.getImageByRoadmap(roadmap);

                RoadmapCrudMessage responseMessage = RoadmapCrudMessage.builder()
                        .action(message.getAction())
                        .roadmapId(roadmap.getId())
                        .title(message.getTitle())
                        .description(message.getDescription())
                        .categories(message.getCategories())
                        .imageUrl(image.getUrl())
                        .isFavorite(roadmap.getIsFavorite())
                        .lastModifiedAt(roadmap.getLastModifiedAt())
                        .lastAccessedAt(roadmap.getLastAccessedAt())
                        .userId(message.getUserId())
                        .userName(message.getUserName())
                        .build();

                return responseMessage;
            }
            case DELETE -> {
                roadmapRepository.deleteById(roadmapId);
                return message;
            }
            default -> throw new InvalidActionException();
        }
    }

}
