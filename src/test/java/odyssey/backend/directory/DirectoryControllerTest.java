package odyssey.backend.directory;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.presentation.directory.dto.request.DirectoryRequest;
import odyssey.backend.presentation.directory.dto.response.DirectoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DirectoryControllerTest extends RestDocsSupport {

    @Test
    void 디렉토리를_생성한다() throws Exception {
        DirectoryRequest request = new DirectoryRequest("새 디렉토리", null);
        DirectoryResponse response = new DirectoryResponse(1L, "새 디렉토리", null, List.of(), List.of());

        given(directoryService.createDirectory(any(DirectoryRequest.class), any()))
                .willReturn(response);

        mvc.perform(post("/directories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("새 디렉토리"))
                .andDo(document("create-directory",
                requestFields(
                        fieldWithPath("name").description("디렉토리 이름"),
                        fieldWithPath("parentId").description("상위 디렉토리 Id")
                ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("디렉토리 ID"),
                                fieldWithPath("data.name").description("디렉토리 이름"),
                                fieldWithPath("data.parentId").description("상위 디렉토리 Id"),
                                fieldWithPath("data.directories").description("하위 디렉토리 리스트"),
                                fieldWithPath("data.roadmaps").description("내부 로드맵 리스트")
                        )
        ));
    }

    @Test
    void 디렉토리를_수정한다() throws Exception {
        Long id = 1L;
        DirectoryRequest request = new DirectoryRequest("수정된 디렉토리", null);
        DirectoryResponse response = new DirectoryResponse(id, "수정된 디렉토리", null, List.of(), List.of());

        given(directoryService.updateDirectory(eq(id), any(DirectoryRequest.class), any()))
                .willReturn(response);

        mvc.perform(put("/directories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.name").value("수정된 디렉토리"))
                        .andDo(document("update-directory",
                                requestFields(
                                        fieldWithPath("name").description("디렉토리 이름"),
                                        fieldWithPath("parentId").description("상위 디렉토리 Id")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data.id").description("디렉토리 ID"),
                                        fieldWithPath("data.name").description("디렉토리 이름"),
                                        fieldWithPath("data.parentId").description("상위 디렉토리 Id"),
                                        fieldWithPath("data.directories").description("하위 디렉토리 리스트"),
                                        fieldWithPath("data.roadmaps").description("내부 로드맵 리스트")
                                )));
    }

    @Test
    void 디렉토리를_삭제한다() throws Exception {
        Long id = 1L;

        mvc.perform(delete("/directories/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent())
        .andDo(document("directory-delete",
                pathParameters(
                        parameterWithName("id").description("삭제할 디렉토리 ID")
                )
        ));
    }

    @Test
    void 팀_디렉토리를_생성한다() throws Exception {
        Long teamId = 1L;
        DirectoryRequest request = new DirectoryRequest("팀 디렉토리", null);
        DirectoryResponse response = new DirectoryResponse(2L, "팀 디렉토리", null, List.of(), List.of());

        given(directoryService.createTeamDirectory(eq(teamId), any(DirectoryRequest.class), any()))
                .willReturn(response);

        mvc.perform(post("/directories/team/{teamId}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(2L))
                .andExpect(jsonPath("$.data.name").value("팀 디렉토리"))
                .andDo(document("create-team-directory",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("디렉토리 이름"),
                                fieldWithPath("parentId").description("상위 디렉토리 Id")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("디렉토리 ID"),
                                fieldWithPath("data.name").description("디렉토리 이름"),
                                fieldWithPath("data.parentId").description("상위 디렉토리 Id"),
                                fieldWithPath("data.directories").description("하위 디렉토리 리스트"),
                                fieldWithPath("data.roadmaps").description("내부 로드맵 리스트")
                        )
                ));
    }

    @Test
    void 팀_디렉토리를_수정한다() throws Exception {
        Long id = 2L;
        Long teamId = 1L;
        DirectoryRequest request = new DirectoryRequest("수정된 팀 디렉토리", null);
        DirectoryResponse response = new DirectoryResponse(id, "수정된 팀 디렉토리", null, List.of(), List.of());

        given(directoryService.updateTeamDirectory(eq(id), eq(teamId), any(DirectoryRequest.class), any()))
                .willReturn(response);

        mvc.perform(put("/directories/{id}/team/{teamId}", id, teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.name").value("수정된 팀 디렉토리"))
                .andDo(document("update-team-directory",
                        pathParameters(
                                parameterWithName("id").description("디렉토리 ID"),
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("디렉토리 이름"),
                                fieldWithPath("parentId").description("상위 디렉토리 Id")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("디렉토리 ID"),
                                fieldWithPath("data.name").description("디렉토리 이름"),
                                fieldWithPath("data.parentId").description("상위 디렉토리 Id"),
                                fieldWithPath("data.directories").description("하위 디렉토리 리스트"),
                                fieldWithPath("data.roadmaps").description("내부 로드맵 리스트")
                        )
                ));
    }

    @Test
    void 팀_디렉토리를_삭제한다() throws Exception {
        Long id = 2L;
        Long teamId = 1L;

        mvc.perform(delete("/directories/{id}/team/{teamId}", id, teamId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("팀 디렉토리가 삭제되었습니다."))
                .andDo(document("delete-team-directory",
                        pathParameters(
                                parameterWithName("id").description("삭제할 디렉토리 ID"),
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("삭제 결과 메시지")
                        )
                ));
    }

}
