package odyssey.backend.application.roadmap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.application.directory.DirectoryService;
import odyssey.backend.domain.directory.Directory;
import odyssey.backend.infrastructure.s3.S3Service;
import odyssey.backend.domain.roadmap.Roadmap;
import odyssey.backend.infrastructure.persistence.roadmap.RoadmapRepository;
import odyssey.backend.presentation.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.presentation.roadmap.dto.response.RoadmapResponse;
import odyssey.backend.domain.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.domain.auth.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
@Component
public class RoadmapFacade {

    private final RoadmapRepository roadmapRepository;
    private final S3Service s3Service;
    private final DirectoryService directoryService;

    @Transactional
    public RoadmapResponse save(RoadmapRequest request, MultipartFile thumbnail, User user){
        Long teamId = request.getTeamId();

        Directory directory = null;

        if (request.getDirectoryId() != null) {
            directory = directoryService.findDirectoryById(request.getDirectoryId());
        }

        String url = s3Service.uploadFile(thumbnail);

        Roadmap roadmap = roadmapRepository.save(
                Roadmap.from(request, url, directory, user, teamId)
        );

        roadmap.updateLastModifiedAt();

        log.info("생성된 로드맵 Id : {}", roadmap.getId());

        return RoadmapResponse.from(roadmap, user.getUuid());
    }

    @Transactional
    public void deleteRoadmapById(Long id) {
        Roadmap roadmap = findRoadmapById(id);

        log.info("삭제된 로드맵 Id : {}", id);

        s3Service.deleteFile(roadmap.getImageUrl());

        roadmapRepository.deleteById(id);
    }

    private Roadmap findRoadmapById(Long id) {
        return roadmapRepository.findById(id)
                .orElseThrow(RoadmapNotFoundException::new);
    }

    @Transactional
    public RoadmapResponse update(Long id, RoadmapRequest request, User user) {
        Roadmap roadmap = findRoadmapById(id);

        Directory directory = directoryService.findDirectoryById(request.getDirectoryId());

        if(!roadmap.getDirectory().getId().equals(directory.getId())) {
            roadmap.changeDirectory(directory);
            roadmap.updateLastModifiedAt();
        }

        roadmap.update(request.getTitle(), request.getDescription(), request.getCategories());

        return RoadmapResponse.from(roadmap, user.getUuid());
    }

    @Transactional
    public RoadmapResponse toggleFavorite(Long id, User user) {
        Roadmap roadmap = findRoadmapById(id);

        roadmap.toggleFavorite();

        log.info("즐겨찾기 요청 로드맵 Id : {}", roadmap.getId());

        return RoadmapResponse.from(roadmap, user.getUuid());
    }

}
