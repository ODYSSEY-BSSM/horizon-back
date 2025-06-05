package odyssey.backend.node.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.node.dto.NodeRequest;
import odyssey.backend.node.dto.NodeResponse;
import odyssey.backend.node.service.NodeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap/{roadmapId}/nodes")
public class NodeController {

    private final NodeService nodeService;

    @PostMapping
    public NodeResponse createNode(
            @PathVariable Long roadmapId, @Valid @RequestBody NodeRequest nodeRequest) {
        return nodeService.createNode(roadmapId, nodeRequest);
    }

    @GetMapping
    public List<NodeResponse> getNodesByRoadmap(@PathVariable Long roadmapId) {
        return nodeService.getNodesByRoadmapId(roadmapId);
    }

    @GetMapping("/{nodeId}")
    public NodeResponse getNode(@PathVariable Long nodeId ,@PathVariable Long roadmapId) {
        return nodeService.getNodeByIdAndRoadmapId(nodeId, roadmapId);
    }

    @PutMapping("/{nodeId}")
    public NodeResponse updateNode(
            @PathVariable Long nodeId, @PathVariable Long roadmapId, @Valid @RequestBody NodeRequest request
    ){
        return nodeService.updateNode(nodeId, roadmapId, request);
    }

    @DeleteMapping("/{nodeId}")
    public void deleteNode(@PathVariable Long nodeId, @PathVariable Long roadmapId) {
        nodeService.deleteNodeByIdAndRoadmapId(nodeId, roadmapId);
    }

}