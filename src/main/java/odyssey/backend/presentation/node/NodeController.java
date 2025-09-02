package odyssey.backend.presentation.node;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.shared.response.CommonResponse;
import odyssey.backend.shared.response.ListCommonResponse;
import odyssey.backend.shared.response.SingleCommonResponse;
import odyssey.backend.presentation.node.dto.request.NodeRequest;
import odyssey.backend.presentation.node.dto.response.NodeResponse;
import odyssey.backend.application.node.NodeService;
import odyssey.backend.domain.auth.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap/{roadmapId}/nodes")
public class NodeController {

    private final NodeService nodeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<NodeResponse> createNode(
            @PathVariable Long roadmapId,
            @Valid @RequestBody NodeRequest nodeRequest,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(nodeService.createNode(roadmapId, nodeRequest));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListCommonResponse<NodeResponse> getNodesByRoadmap(
            @PathVariable Long roadmapId,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(nodeService.getNodesByRoadmapId(roadmapId));
    }

    @GetMapping("/{nodeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<NodeResponse> getNode(
            @PathVariable Long nodeId,
            @PathVariable Long roadmapId,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(nodeService.getNodeByIdAndRoadmapId(nodeId, roadmapId));
    }

    @PutMapping("/{nodeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<NodeResponse> updateNode(
            @PathVariable Long nodeId,
            @PathVariable Long roadmapId,
            @Valid @RequestBody NodeRequest request,
            @AuthenticationPrincipal User user) {
        return CommonResponse.ok(nodeService.updateNode(nodeId, roadmapId, request));
    }

    @DeleteMapping("/{nodeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<String> deleteNode(
            @PathVariable Long nodeId,
            @PathVariable Long roadmapId,
            @AuthenticationPrincipal User user) {
        nodeService.deleteNodeByIdAndRoadmapId(nodeId, roadmapId);

        return CommonResponse.ok("삭제되었습니다.");
    }
}
