package odyssey.backend.team;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.node.domain.NodeType;
import odyssey.backend.node.dto.request.NodeMoveRequest;
import odyssey.backend.node.dto.request.NodeRequest;
import odyssey.backend.node.dto.request.NodeResizeRequest;
import odyssey.backend.node.dto.response.NodeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamNodeControllerTest extends RestDocsSupport {

    @Test
    public void 팀_노드를_생성한다() throws Exception {
        Long teamId = 1L;
        Long roadmapId = 1L;
        NodeRequest request = new NodeRequest("백엔드 학습", "Spring Boot 학습", 80, 150, NodeType.Top, 100, 200, "BACKEND", null);
        
        NodeResponse response = new NodeResponse(1L, "백엔드 학습", "Spring Boot 학습", 80, 150, NodeType.Top, 100, 200, "BACKEND", roadmapId, null, List.of());

        given(teamNodeService.createNode(eq(teamId), eq(roadmapId), any(NodeRequest.class))).willReturn(response);

        mvc.perform(post("/teams/{teamId}/roadmaps/{roadmapId}/nodes", teamId, roadmapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(document("team-node-create",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("roadmapId").description("로드맵 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("노드 제목"),
                                fieldWithPath("description").description("노드 설명"),
                                fieldWithPath("height").description("높이"),
                                fieldWithPath("width").description("너비"),
                                fieldWithPath("type").description("노드 타입"),
                                fieldWithPath("x").description("X 좌표"),
                                fieldWithPath("y").description("Y 좌표"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("parentNodeId").description("부모 노드 ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("노드 ID"),
                                fieldWithPath("data.title").description("노드 제목"),
                                fieldWithPath("data.description").description("노드 설명"),
                                fieldWithPath("data.height").description("높이"),
                                fieldWithPath("data.width").description("너비"),
                                fieldWithPath("data.type").description("노드 타입"),
                                fieldWithPath("data.x").description("X 좌표"),
                                fieldWithPath("data.y").description("Y 좌표"),
                                fieldWithPath("data.category").description("카테고리"),
                                fieldWithPath("data.roadmapId").description("로드맵 ID"),
                                fieldWithPath("data.parentNodeId").description("부모 노드 ID").optional(),
                                fieldWithPath("data.childNode").description("자식 노드 목록").optional()
                        )
                ));
    }

    @Test
    public void 팀_노드를_수정한다() throws Exception {
        Long teamId = 1L;
        Long roadmapId = 1L;
        Long nodeId = 1L;
        NodeRequest request = new NodeRequest("수정된 노드", "수정된 설명", 100, 200, NodeType.Middle, 150, 250, "FRONTEND", null);
        
        NodeResponse response = new NodeResponse(nodeId, "수정된 노드", "수정된 설명", 100, 200, NodeType.Middle, 150, 250, "FRONTEND", roadmapId, null, List.of());

        given(teamNodeService.updateNode(eq(teamId), eq(roadmapId), eq(nodeId), any(NodeRequest.class))).willReturn(response);

        mvc.perform(put("/teams/{teamId}/roadmaps/{roadmapId}/nodes/{nodeId}", teamId, roadmapId, nodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-node-update",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("roadmapId").description("로드맵 ID"),
                                parameterWithName("nodeId").description("노드 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("노드 제목"),
                                fieldWithPath("description").description("노드 설명"),
                                fieldWithPath("height").description("높이"),
                                fieldWithPath("width").description("너비"),
                                fieldWithPath("type").description("노드 타입"),
                                fieldWithPath("x").description("X 좌표"),
                                fieldWithPath("y").description("Y 좌표"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("parentNodeId").description("부모 노드 ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("노드 ID"),
                                fieldWithPath("data.title").description("노드 제목"),
                                fieldWithPath("data.description").description("노드 설명"),
                                fieldWithPath("data.height").description("높이"),
                                fieldWithPath("data.width").description("너비"),
                                fieldWithPath("data.type").description("노드 타입"),
                                fieldWithPath("data.x").description("X 좌표"),
                                fieldWithPath("data.y").description("Y 좌표"),
                                fieldWithPath("data.category").description("카테고리"),
                                fieldWithPath("data.roadmapId").description("로드맵 ID"),
                                fieldWithPath("data.parentNodeId").description("부모 노드 ID").optional(),
                                fieldWithPath("data.childNode").description("자식 노드 목록").optional()
                        )
                ));
    }

    @Test
    public void 팀_노드를_삭제한다() throws Exception {
        Long teamId = 1L;
        Long roadmapId = 1L;
        Long nodeId = 1L;

        mvc.perform(delete("/teams/{teamId}/roadmaps/{roadmapId}/nodes/{nodeId}", teamId, roadmapId, nodeId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-node-delete",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("roadmapId").description("로드맵 ID"),
                                parameterWithName("nodeId").description("노드 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("삭제 결과 메시지")
                        )
                ));
    }

    @Test
    public void 팀_노드를_이동한다() throws Exception {
        Long teamId = 1L;
        Long roadmapId = 1L;
        Long nodeId = 1L;
        NodeMoveRequest request = new NodeMoveRequest(200, 300, null);
        
        NodeResponse response = new NodeResponse(nodeId, "이동된 노드", null, null, null, null, 200, 300, null, null, null, List.of());

        given(teamNodeService.moveNode(eq(teamId), eq(roadmapId), eq(nodeId), any(NodeMoveRequest.class))).willReturn(response);

        mvc.perform(put("/teams/{teamId}/roadmaps/{roadmapId}/nodes/{nodeId}/move", teamId, roadmapId, nodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-node-move",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("roadmapId").description("로드맵 ID"),
                                parameterWithName("nodeId").description("노드 ID")
                        ),
                        requestFields(
                                fieldWithPath("x").description("새로운 X 좌표"),
                                fieldWithPath("y").description("새로운 Y 좌표"),
                                fieldWithPath("parentNodeId").description("부모 노드 ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("노드 ID"),
                                fieldWithPath("data.title").description("노드 제목"),
                                fieldWithPath("data.description").description("노드 설명").optional(),
                                fieldWithPath("data.height").description("높이").optional(),
                                fieldWithPath("data.width").description("너비").optional(),
                                fieldWithPath("data.type").description("노드 타입").optional(),
                                fieldWithPath("data.x").description("X 좌표"),
                                fieldWithPath("data.y").description("Y 좌표"),
                                fieldWithPath("data.category").description("카테고리").optional(),
                                fieldWithPath("data.roadmapId").description("로드맵 ID").optional(),
                                fieldWithPath("data.parentNodeId").description("부모 노드 ID").optional(),
                                fieldWithPath("data.childNode").description("자식 노드 목록").optional()
                        )
                ));
    }

    @Test
    public void 팀_노드_사이즈를_변경한다() throws Exception {
        Long teamId = 1L;
        Long roadmapId = 1L;
        Long nodeId = 1L;
        NodeResizeRequest request = new NodeResizeRequest(250, 120);
        
        NodeResponse response = new NodeResponse(nodeId, "리사이즈된 노드", null, 120, 250, null, null, null, null, null, null, List.of());

        given(teamNodeService.resizeNode(eq(teamId), eq(roadmapId), eq(nodeId), any(NodeResizeRequest.class))).willReturn(response);

        mvc.perform(put("/teams/{teamId}/roadmaps/{roadmapId}/nodes/{nodeId}/resize", teamId, roadmapId, nodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-node-resize",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("roadmapId").description("로드맵 ID"),
                                parameterWithName("nodeId").description("노드 ID")
                        ),
                        requestFields(
                                fieldWithPath("width").description("새로운 너비"),
                                fieldWithPath("height").description("새로운 높이")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("노드 ID"),
                                fieldWithPath("data.title").description("노드 제목"),
                                fieldWithPath("data.description").description("노드 설명").optional(),
                                fieldWithPath("data.height").description("높이"),
                                fieldWithPath("data.width").description("너비"),
                                fieldWithPath("data.type").description("노드 타입").optional(),
                                fieldWithPath("data.x").description("X 좌표").optional(),
                                fieldWithPath("data.y").description("Y 좌표").optional(),
                                fieldWithPath("data.category").description("카테고리").optional(),
                                fieldWithPath("data.roadmapId").description("로드맵 ID").optional(),
                                fieldWithPath("data.parentNodeId").description("부모 노드 ID").optional(),
                                fieldWithPath("data.childNode").description("자식 노드 목록").optional()
                        )
                ));
    }

    @Test
    void 팀_노드_목록을_조회한다() throws Exception {
        Long roadmapId = 1L;
        List<NodeResponse> responses = List.of(
                new NodeResponse(1L, "노드1", "설명1", null, null, null, null, null, null, null, null, List.of()),
                new NodeResponse(2L, "노드2", "설명2", null, null, null, null, null, null, null, null, List.of())
        );

        given(teamNodeService.getNodes(roadmapId)).willReturn(responses);

        mvc.perform(get("/teams/{teamId}/roadmaps/{roadmapId}/nodes", 1L, roadmapId))
                .andExpect(status().isOk())
                .andDo(document("team-node-list",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("roadmapId").description("로드맵 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("노드 ID"),
                                fieldWithPath("data[].title").description("노드 제목"),
                                fieldWithPath("data[].description").description("노드 설명"),
                                fieldWithPath("data[].height").description("높이").optional(),
                                fieldWithPath("data[].width").description("너비").optional(),
                                fieldWithPath("data[].type").description("노드 타입").optional(),
                                fieldWithPath("data[].x").description("X 좌표").optional(),
                                fieldWithPath("data[].y").description("Y 좌표").optional(),
                                fieldWithPath("data[].category").description("카테고리").optional(),
                                fieldWithPath("data[].roadmapId").description("로드맵 ID").optional(),
                                fieldWithPath("data[].parentNodeId").description("부모 노드 ID").optional(),
                                fieldWithPath("data[].childNode").description("자식 노드 목록").optional()
                        )
                ));
    }
}