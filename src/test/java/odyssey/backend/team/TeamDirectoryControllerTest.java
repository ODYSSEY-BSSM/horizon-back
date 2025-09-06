package odyssey.backend.team;

import odyssey.backend.directory.dto.request.DirectoryRequest;
import odyssey.backend.directory.dto.response.DirectoryResponse;
import odyssey.backend.directory.dto.response.RootContentResponse;
import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.roadmap.dto.response.RoadmapResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
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

public class TeamDirectoryControllerTest extends RestDocsSupport {

    @Test
    public void 팀_디렉토리를_생성한다() throws Exception {
        Long teamId = 1L;
        DirectoryRequest request = new DirectoryRequest("백엔드 학습", null);
        
        DirectoryResponse response = new DirectoryResponse(1L, "백엔드 학습", null, List.of(), 
                LocalDateTime.now(), LocalDateTime.now());

        given(teamDirectoryService.createDirectory(eq(teamId), any(DirectoryRequest.class))).willReturn(response);

        mvc.perform(post("/teams/{teamId}/directories", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(document("team-directory-create",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("디렉토리 이름"),
                                fieldWithPath("parentId").description("부모 디렉토리 ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("디렉토리 ID"),
                                fieldWithPath("data.name").description("디렉토리 이름"),
                                fieldWithPath("data.parentId").description("부모 디렉토리 ID").optional(),
                                fieldWithPath("data.children").description("자식 디렉토리 목록").optional(),
                                fieldWithPath("data.createdAt").description("생성 일시"),
                                fieldWithPath("data.updatedAt").description("수정 일시")
                        )
                ));
    }

    @Test
    public void 팀_디렉토리_목록을_조회한다() throws Exception {
        Long teamId = 1L;
        List<DirectoryResponse> responses = List.of(
                new DirectoryResponse(1L, "백엔드", null, List.of(), LocalDateTime.now(), LocalDateTime.now()),
                new DirectoryResponse(2L, "프론트엔드", null, List.of(), LocalDateTime.now(), LocalDateTime.now())
        );

        given(teamDirectoryService.getTeamDirectories(teamId)).willReturn(responses);

        mvc.perform(get("/teams/{teamId}/directories", teamId))
                .andExpect(status().isOk())
                .andDo(document("team-directory-list",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("디렉토리 ID"),
                                fieldWithPath("data[].name").description("디렉토리 이름"),
                                fieldWithPath("data[].parentId").description("부모 디렉토리 ID").optional(),
                                fieldWithPath("data[].children").description("자식 디렉토리 목록").optional(),
                                fieldWithPath("data[].createdAt").description("생성 일시"),
                                fieldWithPath("data[].updatedAt").description("수정 일시")
                        )
                ));
    }

    @Test
    public void 팀_디렉토리를_조회한다() throws Exception {
        Long teamId = 1L;
        Long directoryId = 1L;
        
        DirectoryResponse response = new DirectoryResponse(directoryId, "백엔드 학습", null, List.of(), 
                LocalDateTime.now(), LocalDateTime.now());

        given(teamDirectoryService.getDirectory(teamId, directoryId)).willReturn(response);

        mvc.perform(get("/teams/{teamId}/directories/{directoryId}", teamId, directoryId))
                .andExpect(status().isOk())
                .andDo(document("team-directory-get",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("directoryId").description("디렉토리 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("디렉토리 ID"),
                                fieldWithPath("data.name").description("디렉토리 이름"),
                                fieldWithPath("data.parentId").description("부모 디렉토리 ID").optional(),
                                fieldWithPath("data.children").description("자식 디렉토리 목록").optional(),
                                fieldWithPath("data.createdAt").description("생성 일시"),
                                fieldWithPath("data.updatedAt").description("수정 일시")
                        )
                ));
    }

    @Test
    public void 팀_디렉토리를_수정한다() throws Exception {
        Long teamId = 1L;
        Long directoryId = 1L;
        DirectoryRequest request = new DirectoryRequest("수정된 디렉토리", null);
        
        DirectoryResponse response = new DirectoryResponse(directoryId, "수정된 디렉토리", null, List.of(), 
                LocalDateTime.now(), LocalDateTime.now());

        given(teamDirectoryService.updateDirectory(eq(teamId), eq(directoryId), any(DirectoryRequest.class)))
                .willReturn(response);

        mvc.perform(put("/teams/{teamId}/directories/{directoryId}", teamId, directoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-directory-update",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("directoryId").description("디렉토리 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("디렉토리 이름"),
                                fieldWithPath("parentId").description("부모 디렉토리 ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("디렉토리 ID"),
                                fieldWithPath("data.name").description("디렉토리 이름"),
                                fieldWithPath("data.parentId").description("부모 디렉토리 ID").optional(),
                                fieldWithPath("data.children").description("자식 디렉토리 목록").optional(),
                                fieldWithPath("data.createdAt").description("생성 일시"),
                                fieldWithPath("data.updatedAt").description("수정 일시")
                        )
                ));
    }

    @Test
    public void 팀_디렉토리를_삭제한다() throws Exception {
        Long teamId = 1L;
        Long directoryId = 1L;

        mvc.perform(delete("/teams/{teamId}/directories/{directoryId}", teamId, directoryId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-directory-delete",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("directoryId").description("디렉토리 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("삭제 결과 메시지")
                        )
                ));
    }

    @Test
    public void 팀_루트_콘텐츠를_조회한다() throws Exception {
        Long teamId = 1L;
        
        List<DirectoryResponse> directories = List.of(
                new DirectoryResponse(1L, "백엔드", null, List.of(), LocalDateTime.now(), LocalDateTime.now())
        );
        
        List<RoadmapResponse> roadmaps = List.of(
                new RoadmapResponse(1L, "Java 로드맵", "Java 학습", List.of("Java"), 
                        "https://image.jpg", "2025-08-31", "2025-08-31T23:39:46", false, "팀 루트")
        );
        
        RootContentResponse response = new RootContentResponse(directories, roadmaps);

        given(teamDirectoryService.getRootContents(teamId)).willReturn(response);

        mvc.perform(get("/teams/{teamId}/directories/root-contents", teamId))
                .andExpect(status().isOk())
                .andDo(document("team-directory-root-contents",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.directories").description("디렉토리 목록"),
                                fieldWithPath("data.directories[].id").description("디렉토리 ID"),
                                fieldWithPath("data.directories[].name").description("디렉토리 이름"),
                                fieldWithPath("data.directories[].parentId").description("부모 디렉토리 ID").optional(),
                                fieldWithPath("data.directories[].children").description("자식 디렉토리 목록").optional(),
                                fieldWithPath("data.directories[].createdAt").description("생성 일시"),
                                fieldWithPath("data.directories[].updatedAt").description("수정 일시"),
                                fieldWithPath("data.roadmaps").description("로드맵 목록"),
                                fieldWithPath("data.roadmaps[].id").description("로드맵 ID"),
                                fieldWithPath("data.roadmaps[].title").description("로드맵 제목"),
                                fieldWithPath("data.roadmaps[].description").description("로드맵 설명"),
                                fieldWithPath("data.roadmaps[].categories").description("카테고리 목록"),
                                fieldWithPath("data.roadmaps[].thumbnailUrl").description("썸네일 URL"),
                                fieldWithPath("data.roadmaps[].lastModifiedAt").description("마지막 수정일"),
                                fieldWithPath("data.roadmaps[].lastAccessedAt").description("마지막 접근일"),
                                fieldWithPath("data.roadmaps[].isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data.roadmaps[].location").description("위치")
                        )
                ));
    }

    @Test
    public void 팀_디렉토리_콘텐츠를_조회한다() throws Exception {
        Long teamId = 1L;
        Long directoryId = 1L;
        
        List<DirectoryResponse> directories = List.of(
                new DirectoryResponse(2L, "하위 디렉토리", 1L, List.of(), LocalDateTime.now(), LocalDateTime.now())
        );
        
        List<RoadmapResponse> roadmaps = List.of(
                new RoadmapResponse(1L, "Spring 로드맵", "Spring 학습", List.of("Spring"), 
                        "https://image.jpg", "2025-08-31", "2025-08-31T23:39:46", false, "백엔드")
        );
        
        RootContentResponse response = new RootContentResponse(directories, roadmaps);

        given(teamDirectoryService.getDirectoryContents(teamId, directoryId)).willReturn(response);

        mvc.perform(get("/teams/{teamId}/directories/{directoryId}/roadmaps", teamId, directoryId))
                .andExpect(status().isOk())
                .andDo(document("team-directory-contents",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("directoryId").description("디렉토리 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.directories").description("하위 디렉토리 목록"),
                                fieldWithPath("data.directories[].id").description("디렉토리 ID"),
                                fieldWithPath("data.directories[].name").description("디렉토리 이름"),
                                fieldWithPath("data.directories[].parentId").description("부모 디렉토리 ID").optional(),
                                fieldWithPath("data.directories[].children").description("자식 디렉토리 목록").optional(),
                                fieldWithPath("data.directories[].createdAt").description("생성 일시"),
                                fieldWithPath("data.directories[].updatedAt").description("수정 일시"),
                                fieldWithPath("data.roadmaps").description("로드맵 목록"),
                                fieldWithPath("data.roadmaps[].id").description("로드맵 ID"),
                                fieldWithPath("data.roadmaps[].title").description("로드맵 제목"),
                                fieldWithPath("data.roadmaps[].description").description("로드맵 설명"),
                                fieldWithPath("data.roadmaps[].categories").description("카테고리 목록"),
                                fieldWithPath("data.roadmaps[].thumbnailUrl").description("썸네일 URL"),
                                fieldWithPath("data.roadmaps[].lastModifiedAt").description("마지막 수정일"),
                                fieldWithPath("data.roadmaps[].lastAccessedAt").description("마지막 접근일"),
                                fieldWithPath("data.roadmaps[].isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data.roadmaps[].location").description("위치")
                        )
                ));
    }
}