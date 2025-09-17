    package odyssey.backend.application.roadmap;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import odyssey.backend.domain.auth.User;
    import odyssey.backend.domain.roadmap.Roadmap;
    import odyssey.backend.domain.roadmap.exception.RoadmapNotFoundException;
    import odyssey.backend.infrastructure.persistence.roadmap.RoadmapRepository;
    import odyssey.backend.infrastructure.s3.S3Service;
    import odyssey.backend.presentation.roadmap.dto.response.ImageUrlResponse;
    import odyssey.backend.presentation.roadmap.dto.response.PersonalRoadmapResponse;
    import odyssey.backend.presentation.roadmap.dto.response.RoadmapCountResponse;
    import odyssey.backend.presentation.roadmap.dto.response.TeamRoadmapResponse;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class RoadmapService {

        private final RoadmapRepository roadmapRepository;
        private final S3Service s3Service;


        public List<PersonalRoadmapResponse> findPersonalRoadmaps(User user) {
            return roadmapRepository.findByUserAndTeamIdIsNullOrderByLastAccessedAtDesc(user).stream()
                    .map(roadmap -> PersonalRoadmapResponse.from(roadmap, user.getUuid()))
                    .toList();
        }

        public List<TeamRoadmapResponse> findTeamRoadmaps(User user, Long teamId) {
            return roadmapRepository.findByTeamIdOrderByLastAccessedAtDesc(teamId)
                    .stream()
                    .map(roadmap -> TeamRoadmapResponse.from(roadmap, user.getUuid()))
                    .toList();
        }

        public PersonalRoadmapResponse getLastAccessedRoadmap(User user) {

            Roadmap roadmap = roadmapRepository.findTopByUserOrderByLastAccessedAtDesc(user)
                    .orElseThrow(RoadmapNotFoundException::new);

            return PersonalRoadmapResponse.from(roadmap, user.getUuid());
        }

        public RoadmapCountResponse getRoadmapCount(User user) {
            Long count = roadmapRepository.countByUser(user);

            return RoadmapCountResponse.from(count);
        }

        public ImageUrlResponse getUrlByRoadmapId(Long id) {
            Roadmap roadmap = roadmapRepository.findById(id)
                    .orElseThrow(RoadmapNotFoundException::new);

            return ImageUrlResponse.create(roadmap.getImageUrl());
        }

    }
