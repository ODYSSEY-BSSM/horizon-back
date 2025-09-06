package odyssey.backend.team.controller.node;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.ListCommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import odyssey.backend.node.dto.request.NodeMoveRequest;
import odyssey.backend.node.dto.request.NodeRequest;
import odyssey.backend.node.dto.request.NodeResizeRequest;
import odyssey.backend.node.dto.response.NodeResponse;
import odyssey.backend.team.service.node.TeamNodeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/{teamId}/roadmaps/{roadmapId}/nodes")
public class TeamNodeController {

    private final TeamNodeService teamNodeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<NodeResponse> createTeamNode(
            @PathVariable Long teamId,
            @PathVariable Long roadmapId,
            @RequestBody @Valid NodeRequest request) {
        
        NodeResponse response = teamNodeService.createNode(teamId, roadmapId, request);
        return CommonResponse.ok(response);
    }

    @GetMapping
    public ListCommonResponse<NodeResponse> getTeamNodes(
            @PathVariable Long roadmapId) {

        List<NodeResponse> nodes = teamNodeService.getNodes(roadmapId);
        return CommonResponse.ok(nodes);
    }

    @PutMapping("/{nodeId}")
    public SingleCommonResponse<NodeResponse> updateTeamNode(
            @PathVariable Long teamId,
            @PathVariable Long roadmapId,
            @PathVariable Long nodeId,
            @RequestBody @Valid NodeRequest request) {
        
        NodeResponse response = teamNodeService.updateNode(teamId, roadmapId, nodeId, request);
        return CommonResponse.ok(response);
    }

    @DeleteMapping("/{nodeId}")
    public SingleCommonResponse<String> deleteTeamNode(
            @PathVariable Long teamId,
            @PathVariable Long roadmapId,
            @PathVariable Long nodeId) {
        
        teamNodeService.deleteNode(teamId, roadmapId, nodeId);
        return CommonResponse.ok("팀 노드가 삭제되었습니다.");
    }

    @PutMapping("/{nodeId}/move")
    public SingleCommonResponse<NodeResponse> moveTeamNode(
            @PathVariable Long teamId,
            @PathVariable Long roadmapId,
            @PathVariable Long nodeId,
            @RequestBody @Valid NodeMoveRequest request) {
        
        NodeResponse response = teamNodeService.moveNode(teamId, roadmapId, nodeId, request);
        return CommonResponse.ok(response);
    }


    @PutMapping("/{nodeId}/resize")
    public SingleCommonResponse<NodeResponse> resizeTeamNode(
            @PathVariable Long teamId,
            @PathVariable Long roadmapId,
            @PathVariable Long nodeId,
            @RequestBody @Valid NodeResizeRequest request) {
        
        NodeResponse response = teamNodeService.resizeNode(teamId, roadmapId, nodeId, request);
        return CommonResponse.ok(response);
    }
}