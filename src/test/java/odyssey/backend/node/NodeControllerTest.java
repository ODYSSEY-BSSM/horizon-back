package odyssey.backend.node;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.domain.node.NodeType;
import odyssey.backend.presentation.node.dto.request.NodeRequest;
import odyssey.backend.presentation.node.dto.response.NodeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NodeControllerTest extends RestDocsSupport {

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
                .andDo(document("node-create",
                        pathParameters(
                                parameterWithName("roadmapId").description("로드맵 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("노드 제목"),
                                fieldWithPath("description").description("노드 설명"),
                                fieldWithPath("height").description("높이"),
                                fieldWithPath("width").description("너비"),
                                fieldWithPath("type").description("노드 타입"),
                                fieldWithPath("x").description("x 좌표"),
                                fieldWithPath("y").description("y 좌표"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("parentNodeId").optional().description("부모 노드 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("노드 ID"),
                                fieldWithPath("data.title").description("노드 제목"),
                                fieldWithPath("data.description").description("노드 설명"),
                                fieldWithPath("data.height").description("노드 높이"),
                                fieldWithPath("data.width").description("노드 너비"),
                                fieldWithPath("data.type").description("노드 타입"),
                                fieldWithPath("data.x").description("노드 X 좌표"),
                                fieldWithPath("data.y").description("노드 Y 좌표"),
                                fieldWithPath("data.category").description("카테고리"),
                                fieldWithPath("data.roadmapId").description("로드맵 ID"),
                                fieldWithPath("data.parentNodeId").optional().description("부모 노드 ID"),
                                fieldWithPath("data.childNode").optional().description("자식 노드 목록")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 부모가_있는_노드를_조회한다() throws Exception {
        Long roadmapId = 1L;
        Long nodeId = 2L;

        NodeResponse childNode = new NodeResponse(
                2L, "자식 노드 제목", "설명", 1, 2, NodeType.Top, 50, 60, "java", roadmapId, 1L, null
        );

        given(nodeService.getNodeByIdAndRoadmapId(nodeId, roadmapId)).willReturn(childNode);

        mvc.perform(get("/roadmap/{roadmapId}/nodes/{nodeId}", roadmapId, nodeId))
                .andExpect(status().isOk())
                .andDo(document("node-get-with-parent",
                        pathParameters(
                                parameterWithName("roadmapId").description("로드맵 ID"),
                                parameterWithName("nodeId").description("노드 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("노드 ID"),
                                fieldWithPath("data.title").description("노드 제목"),
                                fieldWithPath("data.description").description("노드 설명"),
                                fieldWithPath("data.height").description("노드 높이"),
                                fieldWithPath("data.width").description("노드 너비"),
                                fieldWithPath("data.type").description("노드 타입"),
                                fieldWithPath("data.x").description("노드 X 좌표"),
                                fieldWithPath("data.y").description("노드 Y 좌표"),
                                fieldWithPath("data.category").description("카테고리"),
                                fieldWithPath("data.roadmapId").description("로드맵 ID"),
                                fieldWithPath("data.parentNodeId").description("부모 노드 ID"),
                                fieldWithPath("data.childNode").optional().description("자식 노드 목록")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 노드를_전체조회한다() throws Exception {
        Long roadmapId = 1L;

        NodeResponse parentNode = new NodeResponse(1L, "부모 노드", "부모 설명", 100, 200, NodeType.Top, 50, 60, "java", roadmapId, null, List.of());
        NodeResponse childNode = new NodeResponse(2L, "자식 노드", "자식 설명", 110, 210, NodeType.Top, 60, 70, "java", roadmapId, 1L, null);

        given(nodeService.getNodesByRoadmapId(roadmapId)).willReturn(List.of(parentNode, childNode));

        mvc.perform(get("/roadmap/{roadmapId}/nodes", roadmapId))
                .andExpect(status().isOk())
                .andDo(document("node-get-all",
                        pathParameters(
                                parameterWithName("roadmapId").description("로드맵 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("노드 ID"),
                                fieldWithPath("data[].title").description("노드 제목"),
                                fieldWithPath("data[].description").description("노드 설명"),
                                fieldWithPath("data[].height").description("노드 높이"),
                                fieldWithPath("data[].width").description("노드 너비"),
                                fieldWithPath("data[].type").description("노드 타입"),
                                fieldWithPath("data[].x").description("노드 X 좌표"),
                                fieldWithPath("data[].y").description("노드 Y 좌표"),
                                fieldWithPath("data[].category").description("카테고리"),
                                fieldWithPath("data[].roadmapId").description("로드맵 ID"),
                                fieldWithPath("data[].parentNodeId").optional().description("부모 노드 ID"),
                                fieldWithPath("data[].childNode").optional().description("자식 노드 목록")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 단일_노드를_조회한다() throws Exception {
        Long roadmapId = 1L;
        Long nodeId = 2L;

        NodeResponse node = new NodeResponse(2L, "자식 노드", "자식 설명", 110, 210, NodeType.Top, 60, 70, "java", roadmapId, 1L, null);

        given(nodeService.getNodeByIdAndRoadmapId(nodeId, roadmapId)).willReturn(node);

        mvc.perform(get("/roadmap/{roadmapId}/nodes/{nodeId}", roadmapId, nodeId))
                .andExpect(status().isOk())
                .andDo(document("node-get-one",
                        pathParameters(
                                parameterWithName("roadmapId").description("로드맵 ID"),
                                parameterWithName("nodeId").description("노드 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("노드 ID"),
                                fieldWithPath("data.title").description("노드 제목"),
                                fieldWithPath("data.description").description("노드 설명"),
                                fieldWithPath("data.height").description("노드 높이"),
                                fieldWithPath("data.width").description("노드 너비"),
                                fieldWithPath("data.type").description("노드 타입"),
                                fieldWithPath("data.x").description("노드 X 좌표"),
                                fieldWithPath("data.y").description("노드 Y 좌표"),
                                fieldWithPath("data.category").description("카테고리"),
                                fieldWithPath("data.roadmapId").description("로드맵 ID"),
                                fieldWithPath("data.parentNodeId").description("부모 노드 ID"),
                                fieldWithPath("data.childNode").optional().description("자식 노드 목록")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 노드를_수정한다() throws Exception {
        Long roadmapId = 1L;
        Long nodeId = 2L;

        NodeRequest request = new NodeRequest("수정된 노드", "수정 설명", 120, 220, NodeType.Top, 70, 80, "java", 1L);
        NodeResponse response = new NodeResponse(nodeId, request.getTitle(), request.getDescription(), request.getHeight(),
                request.getWidth(), request.getType(), request.getX(), request.getY(), request.getCategory(),
                roadmapId, request.getParentNodeId(), null);

        given(nodeService.updateNode(eq(nodeId), eq(roadmapId), any(NodeRequest.class))).willReturn(response);

        mvc.perform(put("/roadmap/{roadmapId}/nodes/{nodeId}", roadmapId, nodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("node-update",
                        pathParameters(
                                parameterWithName("roadmapId").description("로드맵 ID"),
                                parameterWithName("nodeId").description("노드 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("노드 제목"),
                                fieldWithPath("description").description("노드 설명"),
                                fieldWithPath("height").description("높이"),
                                fieldWithPath("width").description("너비"),
                                fieldWithPath("type").description("노드 타입"),
                                fieldWithPath("x").description("x 좌표"),
                                fieldWithPath("y").description("y 좌표"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("parentNodeId").description("부모 노드 ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("노드 ID"),
                                fieldWithPath("data.title").description("노드 제목"),
                                fieldWithPath("data.description").description("노드 설명"),
                                fieldWithPath("data.height").description("노드 높이"),
                                fieldWithPath("data.width").description("노드 너비"),
                                fieldWithPath("data.type").description("노드 타입"),
                                fieldWithPath("data.x").description("노드 X 좌표"),
                                fieldWithPath("data.y").description("노드 Y 좌표"),
                                fieldWithPath("data.category").description("카테고리"),
                                fieldWithPath("data.roadmapId").description("로드맵 ID"),
                                fieldWithPath("data.parentNodeId").description("부모 노드 ID").optional(),
                                fieldWithPath("data.childNode").optional().description("자식 노드 목록")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 노드를_삭제한다() throws Exception {
        Long roadmapId = 1L;
        Long nodeId = 2L;

        mvc.perform(delete("/roadmap/{roadmapId}/nodes/{nodeId}", roadmapId, nodeId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("node-delete",
                        pathParameters(
                                parameterWithName("roadmapId").description("로드맵 ID"),
                                parameterWithName("nodeId").description("노드 ID")
                        )
                ));
    }
}
