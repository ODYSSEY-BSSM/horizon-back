package odyssey.backend.presentation.roadmap;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.application.roadmap.RoadmapFacade;
import odyssey.backend.application.roadmap.RoadmapService;
import odyssey.backend.domain.auth.User;
import odyssey.backend.presentation.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.presentation.roadmap.dto.response.PersonalRoadmapResponse;
import odyssey.backend.presentation.roadmap.dto.response.RoadmapCountResponse;
import odyssey.backend.shared.response.CommonResponse;
import odyssey.backend.shared.response.ListCommonResponse;
import odyssey.backend.shared.response.SingleCommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap")
public class RoadmapController {

    private final RoadmapService roadmapService;
    private final RoadmapFacade roadmapFacade;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListCommonResponse<PersonalRoadmapResponse> getPersonalRoadmaps(
            @AuthenticationPrincipal User user
    ) {
        List<PersonalRoadmapResponse> roadmaps = roadmapService.findPersonalRoadmaps(user);
        return CommonResponse.ok(roadmaps);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public SingleCommonResponse<PersonalRoadmapResponse> createRoadmap(
            @RequestBody @Valid RoadmapRequest request,
            @AuthenticationPrincipal User user){
        return CommonResponse.ok(roadmapFacade.savePersonalRoadmap(request, user));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<PersonalRoadmapResponse> updateRoadmap(
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
    public SingleCommonResponse<PersonalRoadmapResponse> toggleFavorite(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(roadmapFacade.toggleFavorite(id, user));
    }

    @GetMapping("/last-accessed")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<PersonalRoadmapResponse> getLastAccessedRoadmap(
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

}
