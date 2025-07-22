package odyssey.backend.node.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.ListCommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import odyssey.backend.node.dto.request.NodeRequest;
import odyssey.backend.node.dto.response.NodeResponse;
import odyssey.backend.node.service.NodeService;
import org.springframework.http.HttpStatus;
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
            @Valid @RequestBody NodeRequest nodeRequest) {
        return CommonResponse.ok(nodeService.createNode(roadmapId, nodeRequest));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListCommonResponse<NodeResponse> getNodesByRoadmap(@PathVariable Long roadmapId) {
        return CommonResponse.ok(nodeService.getNodesByRoadmapId(roadmapId));
    }

    @GetMapping("/{nodeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<NodeResponse> getNode(
            @PathVariable Long nodeId,
            @PathVariable Long roadmapId) {
        return CommonResponse.ok(nodeService.getNodeByIdAndRoadmapId(nodeId, roadmapId));
    }

    @PutMapping("/{nodeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<NodeResponse> updateNode(
            @PathVariable Long nodeId,
            @PathVariable Long roadmapId,
            @Valid @RequestBody NodeRequest request) {
        return CommonResponse.ok(nodeService.updateNode(nodeId, roadmapId, request));
    }

    @DeleteMapping("/{nodeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<String> deleteNode(
            @PathVariable Long nodeId,
            @PathVariable Long roadmapId) {
        nodeService.deleteNodeByIdAndRoadmapId(nodeId, roadmapId);

        return CommonResponse.ok("삭제되었습니다.");
    }
}
