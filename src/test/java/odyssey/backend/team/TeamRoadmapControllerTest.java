package odyssey.backend.team;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.team.dto.response.TeamRoadmapResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamRoadmapControllerTest extends RestDocsSupport {

    @Test
    public void 팀_로드맵을_생성한다() throws Exception {
        Long teamId = 1L;
        RoadmapRequest request = new RoadmapRequest("Java 학습 로드맵", "Java 기초부터 심화까지", List.of("Java", "Spring"), 1L);
        
        MockMultipartFile roadmapPart = new MockMultipartFile(
                "roadmap", "", "application/json", objectMapper.writeValueAsBytes(request));
        MockMultipartFile thumbnail = new MockMultipartFile(
                "thumbnail", "test.jpg", "image/jpeg", "test image".getBytes());

        TeamRoadmapResponse response = new TeamRoadmapResponse(1L, "Java 학습 로드맵", "Java 기초부터 심화까지", 
                List.of("Java", "Spring"), "https://s3.example.com/image.jpg", LocalDate.now(), LocalDateTime.now(), false, "개발팀", teamId, "개발팀");

        given(teamRoadmapService.createRoadmap(eq(teamId), any(RoadmapRequest.class), any()))
                .willReturn(response);

        mvc.perform(multipart("/teams/{teamId}/roadmaps", teamId)
                        .file(roadmapPart)
                        .file(thumbnail)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(document("team-roadmap-create",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        requestParts(
                                partWithName("roadmap").description("로드맵 정보"),
                                partWithName("thumbnail").description("로드맵 썸네일 이미지")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("로드맵 ID"),
                                fieldWithPath("data.title").description("로드맵 제목"),
                                fieldWithPath("data.description").description("로드맵 설명"),
                                fieldWithPath("data.categories").description("카테고리 목록"),
                                fieldWithPath("data.thumbnailUrl").description("썸네일 이미지 URL"),
                                fieldWithPath("data.lastModifiedAt").description("마지막 수정일"),
                                fieldWithPath("data.lastAccessedAt").description("마지막 접근일"),
                                fieldWithPath("data.isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data.location").description("위치"),
                                fieldWithPath("data.teamId").description("팀 ID"),
                                fieldWithPath("data.teamName").description("팀 이름")
                        )
                ));
    }

    @Test
    public void 팀_로드맵을_수정한다() throws Exception {
        Long teamId = 1L;
        Long roadmapId = 1L;
        RoadmapRequest request = new RoadmapRequest("수정된 로드맵", "수정된 설명", List.of("Java", "Spring", "JPA"), 2L);
        
        TeamRoadmapResponse response = new TeamRoadmapResponse(roadmapId, "수정된 로드맵", "수정된 설명", 
                List.of("Java", "Spring", "JPA"), "https://s3.example.com/image.jpg", LocalDate.now(), LocalDateTime.now(), false, "개발팀", teamId, "개발팀");

        given(teamRoadmapService.updateRoadmap(eq(teamId), eq(roadmapId), any(RoadmapRequest.class))).willReturn(response);

        mvc.perform(put("/teams/{teamId}/roadmaps/{roadmapId}", teamId, roadmapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-roadmap-update",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("roadmapId").description("로드맵 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("로드맵 제목"),
                                fieldWithPath("description").description("로드맵 설명"),
                                fieldWithPath("categories").description("카테고리 목록"),
                                fieldWithPath("directoryId").description("디렉토리 ID").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("로드맵 ID"),
                                fieldWithPath("data.title").description("로드맵 제목"),
                                fieldWithPath("data.description").description("로드맵 설명"),
                                fieldWithPath("data.categories").description("카테고리 목록"),
                                fieldWithPath("data.thumbnailUrl").description("썸네일 이미지 URL"),
                                fieldWithPath("data.lastModifiedAt").description("마지막 수정일"),
                                fieldWithPath("data.lastAccessedAt").description("마지막 접근일"),
                                fieldWithPath("data.isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data.location").description("위치"),
                                fieldWithPath("data.teamId").description("팀 ID"),
                                fieldWithPath("data.teamName").description("팀 이름")
                        )
                ));
    }

    @Test
    void 팀_로드맵을_삭제한다() throws Exception {
        Long teamId = 1L;
        Long roadmapId = 1L;

        mvc.perform(delete("/teams/{teamId}/roadmaps/{roadmapId}", teamId, roadmapId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-roadmap-delete",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("roadmapId").description("로드맵 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("삭제 결과 메시지")
                        )
                ));
    }

    @Test
    void 팀_로드맵_목록을_조회한다() throws Exception {
        Long teamId = 1L;
        List<TeamRoadmapResponse> responses = List.of(
                new TeamRoadmapResponse(1L, "Java 로드맵", "Java 학습", List.of("Java"), "url1", LocalDate.now(), LocalDateTime.now(), false, "개발팀", teamId, "개발팀"),
                new TeamRoadmapResponse(2L, "Spring 로드맵", "Spring 학습", List.of("Spring"), "url2", LocalDate.now(), LocalDateTime.now(), false, "개발팀", teamId, "개발팀")
        );

        given(teamRoadmapService.getTeamRoadmaps(teamId)).willReturn(responses);

        mvc.perform(get("/teams/{teamId}/roadmaps", teamId))
                .andExpect(status().isOk())
                .andDo(document("team-roadmap-list",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("로드맵 ID"),
                                fieldWithPath("data[].title").description("로드맵 제목"),
                                fieldWithPath("data[].description").description("로드맵 설명"),
                                fieldWithPath("data[].categories").description("카테고리 목록"),
                                fieldWithPath("data[].thumbnailUrl").description("썸네일 이미지 URL"),
                                fieldWithPath("data[].lastModifiedAt").description("마지막 수정일"),
                                fieldWithPath("data[].lastAccessedAt").description("마지막 접근일"),
                                fieldWithPath("data[].isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data[].location").description("위치"),
                                fieldWithPath("data[].teamId").description("팀 ID"),
                                fieldWithPath("data[].teamName").description("팀 이름")
                        )
                ));
    }

    @Test
    void 팀_로드맵을_조회한다() throws Exception {
        Long teamId = 1L;
        Long roadmapId = 1L;
        
        TeamRoadmapResponse response = new TeamRoadmapResponse(roadmapId, "Java 학습 로드맵", "Java 기초부터 심화까지", 
                List.of("Java", "Spring"), "https://s3.example.com/image.jpg", LocalDate.now(), LocalDateTime.now(), false, "개발팀", teamId, "개발팀");

        given(teamRoadmapService.getRoadmap(teamId, roadmapId)).willReturn(response);

        mvc.perform(get("/teams/{teamId}/roadmaps/{roadmapId}", teamId, roadmapId))
                .andExpect(status().isOk())
                .andDo(document("team-roadmap-get",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID"),
                                parameterWithName("roadmapId").description("로드맵 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("로드맵 ID"),
                                fieldWithPath("data.title").description("로드맵 제목"),
                                fieldWithPath("data.description").description("로드맵 설명"),
                                fieldWithPath("data.categories").description("카테고리 목록"),
                                fieldWithPath("data.thumbnailUrl").description("썸네일 이미지 URL"),
                                fieldWithPath("data.lastModifiedAt").description("마지막 수정일"),
                                fieldWithPath("data.lastAccessedAt").description("마지막 접근일"),
                                fieldWithPath("data.isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data.location").description("위치"),
                                fieldWithPath("data.teamId").description("팀 ID"),
                                fieldWithPath("data.teamName").description("팀 이름")
                        )
                ));
    }
}