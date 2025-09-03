package odyssey.backend.presentation.roadmap;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.shared.response.CommonResponse;
import odyssey.backend.shared.response.ListCommonResponse;
import odyssey.backend.shared.response.SingleCommonResponse;
import odyssey.backend.presentation.roadmap.dto.response.ImageUrlResponse;
import odyssey.backend.presentation.roadmap.dto.response.RoadmapCountResponse;
import odyssey.backend.presentation.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.presentation.roadmap.dto.response.RoadmapResponse;
import odyssey.backend.application.roadmap.RoadmapFacade;
import odyssey.backend.application.roadmap.RoadmapService;
import odyssey.backend.domain.auth.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap")
public class RoadmapController {

    private final RoadmapService roadmapService;
    private final RoadmapFacade roadmapFacade;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListCommonResponse<RoadmapResponse> getPersonalRoadmaps(
            @AuthenticationPrincipal User user
    ) {
        List<RoadmapResponse> roadmaps = roadmapService.findPersonalRoadmaps(user);
        return CommonResponse.ok(roadmaps);
    }

    @GetMapping("/team")
    @ResponseStatus(HttpStatus.OK)
    public ListCommonResponse<RoadmapResponse> getTeamRoadmaps(
            @AuthenticationPrincipal User user,
            @RequestParam(required = true) Long teamId
    ){
        return CommonResponse.ok(roadmapService.findTeamRoadmaps(user, teamId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public SingleCommonResponse<RoadmapResponse> createRoadmap(
            @RequestPart("roadmap") @Valid RoadmapRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @AuthenticationPrincipal User user){
        return CommonResponse.ok(roadmapFacade.save(request, thumbnail, user));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RoadmapResponse> updateRoadmap(
            @PathVariable Long id,
            @RequestBody @Valid RoadmapRequest request,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(roadmapFacade.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<String> deleteRoadmap(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        roadmapFacade.deleteRoadmapById(id);

        return CommonResponse.ok("삭제되었습니다.");
    }

    @PostMapping("/{id}/favorite")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RoadmapResponse> toggleFavorite(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(roadmapFacade.toggleFavorite(id, user));
    }

    @GetMapping("/last-accessed")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RoadmapResponse> getLastAccessedRoadmap(
            @AuthenticationPrincipal User user
    ) {
        return CommonResponse.ok(roadmapService.getLastAccessedRoadmap(user));
    }

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<RoadmapCountResponse> getRoadmapCount(
            @AuthenticationPrincipal User user
    ) {
        return CommonResponse.ok(roadmapService.getRoadmapCount(user));
    }

    @GetMapping("/{id}/url")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<ImageUrlResponse> getThumbnailUrl(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(roadmapService.getUrlByRoadmapId(id));
    }
}
