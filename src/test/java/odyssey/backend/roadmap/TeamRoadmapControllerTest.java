package odyssey.backend.roadmap;
import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.roadmap.Color;
import odyssey.backend.domain.roadmap.Icon;
import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.presentation.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.presentation.roadmap.dto.response.TeamRoadmapResponse;
import odyssey.backend.shared.test.UserCreate;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamRoadmapControllerTest extends RestDocsSupport {

    @Test
    void 팀_로드맵을_조회한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        Long teamId = 1L;

        TeamRoadmapResponse response1 = new TeamRoadmapResponse(
                1L,
                "제목제목",
                "설명",
                List.of("DNDND"),
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                testUser.getUuid(),
                Color.ORANGE.getDescription(),
                Icon.DATABASE.getDescription(),
                teamId,
                "이건우",
                100
        );

        TeamRoadmapResponse response2 = new TeamRoadmapResponse(
                1L,
                "제목제목",
                "설명",
                List.of("DNDND"),
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                testUser.getUuid(),
                Color.ORANGE.getDescription(),
                Icon.DATABASE.getDescription(),
                teamId,
                "이건우",
                100
        );

        given(roadmapService.findTeamRoadmaps(testUser, teamId))
                .willReturn(List.of(response1, response2));

        mvc.perform(get("/teams/{teamId}/roadmap", teamId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andDo(document("roadmap-get-team",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("로드맵 ID"),
                                fieldWithPath("data[].title").description("로드맵 제목"),
                                fieldWithPath("data[].description").description("로드맵 설명"),
                                fieldWithPath("data[].categories").description("카테고리 리스트"),
                                fieldWithPath("data[].lastModifiedAt").description("마지막 수정 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("data[].lastAccessedAt").description("마지막 접속 일시 (yyyy-MM-ddTHH:mm:ss)"),
                                fieldWithPath("data[].isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data[].uuid").description("작성자 UUID"),
                                fieldWithPath("data[].color").description("색깔 enum"),
                                fieldWithPath("data[].icon").description("아이콘 enum"),
                                fieldWithPath("data[].teamId").description("팀 ID"),
                                fieldWithPath("data[].teamName").description("팀 이름"),
                                fieldWithPath(("data[].progress")).optional().description("진행도(문제가 없을 땐 0)")
                        )
                ));
    }

    @Test
    void 팀_로드맵을_생성한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        Long teamId = 1L;

        RoadmapRequest request = new RoadmapRequest(
                "팀 타이틀 생성",
                "팀 설명 생성",
                List.of("카테고리1", "카테고리2"),
                1L,
                Color.BLUE,
                Icon.DATABASE
        );

        TeamRoadmapResponse createdResponse = new TeamRoadmapResponse(
                1L,
                "제목제목",
                "설명",
                List.of("DNDND"),
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                testUser.getUuid(),
                Color.ORANGE.getDescription(),
                Icon.DATABASE.getDescription(),
                teamId,
                "이건우",
                100
        );

        given(roadmapFacade.saveTeamRoadmap(
                any(RoadmapRequest.class),
                any(User.class),
                eq(teamId)
        )).willReturn(createdResponse);

        mvc.perform(post("/teams/{teamId}/roadmap", teamId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("roadmap-create-team",
                        requestFields(
                                fieldWithPath("title").description("로드맵 제목"),
                                fieldWithPath("description").description("로드맵 설명"),
                                fieldWithPath("categories").description("카테고리 리스트"),
                                fieldWithPath("directoryId").description("디렉토리 ID"),
                                fieldWithPath("color").description("색깔 enum"),
                                fieldWithPath("icon").description("아이콘 enum")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("로드맵 ID"),
                                fieldWithPath("data.title").description("로드맵 제목"),
                                fieldWithPath("data.description").description("로드맵 설명"),
                                fieldWithPath("data.categories").description("카테고리 리스트"),
                                fieldWithPath("data.lastModifiedAt").description("마지막 수정 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("data.lastAccessedAt").description("마지막 접속 일시 (yyyy-MM-ddTHH:mm:ss)"),
                                fieldWithPath("data.isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data.uuid").description("작성자 UUID"),
                                fieldWithPath("data.color").description("색깔 enum"),
                                fieldWithPath("data.icon").description("아이콘 enum"),
                                fieldWithPath("data.teamId").description("팀 ID"),
                                fieldWithPath("data.teamName").description("팀 이름"),
                                fieldWithPath(("data.progress")).optional().description("진행도(문제가 없을 땐 0)")
                        )
                ));
    }


}
