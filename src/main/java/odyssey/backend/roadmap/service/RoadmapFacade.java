package odyssey.backend.roadmap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.directory.domain.Directory;
import odyssey.backend.directory.service.DirectoryService;
import odyssey.backend.global.s3.S3Service;
import odyssey.backend.roadmap.domain.Roadmap;
import odyssey.backend.roadmap.domain.RoadmapRepository;
import odyssey.backend.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.roadmap.dto.response.RoadmapResponse;
import odyssey.backend.roadmap.exception.RoadmapNotFoundException;
import odyssey.backend.user.domain.User;
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

        Directory directory = null;

        if (request.getDirectoryId() != null) {
            directory = directoryService.findDirectoryById(request.getDirectoryId());
        }

        String url = s3Service.uploadFile(thumbnail);

        Roadmap roadmap = roadmapRepository.save(
                Roadmap.from(request, url, directory, user)
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

        roadmap.update(request.getTitle(), request.getDescription(), request.getCategories());

        roadmap.updateLastModifiedAt();

        log.info("업데이트 요청 로드맵 Id : {}", roadmap.getId());

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
