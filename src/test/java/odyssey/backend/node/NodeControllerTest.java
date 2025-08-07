package odyssey.backend.node;

import odyssey.backend.global.ControllerTest;
import odyssey.backend.node.domain.NodeType;
import odyssey.backend.node.dto.request.NodeRequest;
import odyssey.backend.node.dto.response.NodeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NodeControllerTest extends ControllerTest {

    @WithMockUser
    @Test
    void 노드를_생성한다() throws Exception {
        Long roadmapId = 1L;
        NodeRequest request = new NodeRequest("노드 제목", "노드 설명", 100, 200,
                NodeType.Top, 50, 60, "Java", null);

        NodeResponse response = new NodeResponse(1L, request.getTitle(), request.getDescription(),
                request.getHeight(), request.getWidth(), request.getType(), request.getX(), request.getY(), request.getCategory(),
                roadmapId, null, null);

        given(nodeService.createNode(eq(roadmapId), any(NodeRequest.class)))
                .willReturn(response);

        mvc.perform(post("/roadmap/{roadmapId}/nodes", roadmapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("노드 제목"));
    }

    @WithMockUser
    @Test
    void 부모가_있는_노드를_조회한다() throws Exception {
        Long roadmapId = 1L;
        Long nodeId = 2L;

        NodeResponse parentNode = new NodeResponse(
                1L, "부모 노드 제목", "설명", 1, 2, NodeType.Top, 50, 60, "Java", roadmapId, null, List.of()
        );

        NodeResponse childNode = new NodeResponse(
                2L, "자식 노드 제목", "설명", 1, 2, NodeType.Top, 50, 60, "java", roadmapId, 1L, null
        );

        given(nodeService.getNodeByIdAndRoadmapId(nodeId, roadmapId)).willReturn(childNode);

        mvc.perform(get("/roadmap/{roadmapId}/nodes/{nodeId}", roadmapId, nodeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(nodeId))
                .andExpect(jsonPath("$.data.title").value("자식 노드 제목"))
                .andExpect(jsonPath("$.data.parentNodeId").value(1L));
    }

    @WithMockUser
    @Test
    void 노드를_전체조회한다() throws Exception {
        Long roadmapId = 1L;

        NodeResponse parentNode = new NodeResponse(
                1L, "부모 노드", "부모 설명", 100, 200, NodeType.Top, 50, 60, "java", roadmapId, null, List.of()
        );

        NodeResponse childNode = new NodeResponse(
                2L, "자식 노드", "자식 설명", 110, 210, NodeType.Top, 60, 70, "java", roadmapId, 1L, null
        );

        given(nodeService.getNodesByRoadmapId(roadmapId))
                .willReturn(List.of(parentNode, childNode));

        mvc.perform(get("/roadmap/{roadmapId}/nodes", roadmapId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("부모 노드"))
                .andExpect(jsonPath("$.data[0].parentNodeId").doesNotExist())
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].title").value("자식 노드"))
                .andExpect(jsonPath("$.data[1].parentNodeId").value(1L));
    }

    @WithMockUser
    @Test
    void 단일_노드를_조회한다() throws Exception {
        Long roadmapId = 1L;
        Long nodeId = 2L;

        NodeResponse node = new NodeResponse(
                2L, "자식 노드", "자식 설명", 110, 210, NodeType.Top, 60, 70, "java", roadmapId, 1L, null
        );

        given(nodeService.getNodeByIdAndRoadmapId(nodeId, roadmapId)).willReturn(node);

        mvc.perform(get("/roadmap/{roadmapId}/nodes/{nodeId}", roadmapId, nodeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(nodeId))
                .andExpect(jsonPath("$.data.title").value("자식 노드"))
                .andExpect(jsonPath("$.data.parentNodeId").value(1L));
    }

    @WithMockUser
    @Test
    void 노드를_수정한다() throws Exception {
        Long roadmapId = 1L;
        Long nodeId = 2L;

        NodeRequest request = new NodeRequest("수정된 노드", "수정 설명", 120, 220, NodeType.Top, 70, 80, "java", 1L);

        NodeResponse response = new NodeResponse(
                nodeId,
                request.getTitle(),
                request.getDescription(),
                request.getHeight(),
                request.getWidth(),
                request.getType(),
                request.getX(),
                request.getY(),
                request.getCategory(),
                roadmapId,
                request.getParentNodeId(),
                null
        );

        given(nodeService.updateNode(eq(nodeId), eq(roadmapId), any(NodeRequest.class)))
                .willReturn(response);

        mvc.perform(put("/roadmap/{roadmapId}/nodes/{nodeId}", roadmapId, nodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(nodeId))
                .andExpect(jsonPath("$.data.title").value("수정된 노드"))
                .andExpect(jsonPath("$.data.parentNodeId").value(1L));
    }

    @WithMockUser
    @Test
    void 노드를_삭제한다() throws Exception {
        Long roadmapId = 1L;
        Long nodeId = 2L;

        mvc.perform(delete("/roadmap/{roadmapId}/nodes/{nodeId}", roadmapId, nodeId)
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
