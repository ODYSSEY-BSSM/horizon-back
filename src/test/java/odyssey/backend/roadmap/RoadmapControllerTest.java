package odyssey.backend.roadmap;

import odyssey.backend.domain.auth.User;
import odyssey.backend.domain.roadmap.Color;
import odyssey.backend.domain.roadmap.Icon;
import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.presentation.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.presentation.roadmap.dto.response.PersonalRoadmapResponse;
import odyssey.backend.presentation.roadmap.dto.response.RoadmapCountResponse;
import odyssey.backend.shared.test.UserCreate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoadmapControllerTest extends RestDocsSupport {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void 로드맵을_생성한다() throws Exception {
        RoadmapRequest request = new RoadmapRequest("자바자바", "조아요", List.of("백엔드", "스프링"), 1L, Color.RED, Icon.DATABASE);
        User testUser = UserCreate.createUser();

        PersonalRoadmapResponse fakeResponse = new PersonalRoadmapResponse(
                1L,
                request.getTitle(),
                request.getDescription(),
                request.getCategories(),
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                testUser.getUuid(),
                request.getColor().getDescription(),
                request.getIcon().getDescription(),
                12
        );

        given(roadmapFacade.savePersonalRoadmap(any(RoadmapRequest.class), any(User.class)))
                .willReturn(fakeResponse);

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        mvc.perform(post("/roadmap")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void 로드맵을_전체조회한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        PersonalRoadmapResponse response1 = new PersonalRoadmapResponse(
                1L, "타이틀1", "설명1", List.of("테스트1", "테스트2"),
                LocalDate.now(), LocalDateTime.now(), true, testUser.getUuid(),
                Color.RED.getDescription(), Icon.HTML.getDescription(), 12
        );

        PersonalRoadmapResponse response2 = new PersonalRoadmapResponse(
                1L, "타이틀1", "설명1", List.of("테스트1", "테스트2"),
                LocalDate.now(), LocalDateTime.now(), true, testUser.getUuid(),
                Color.RED.getDescription(), Icon.HTML.getDescription(), 12
        );

        given(roadmapService.findPersonalRoadmaps(any(User.class)))
                .willReturn(List.of(response1, response2));

        mvc.perform(get("/roadmap")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 로드맵_즐겨찾기를_토글한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        Long roadmapId = 1L;
        PersonalRoadmapResponse fakeResponse = new PersonalRoadmapResponse(
                roadmapId,
                "즐겨찾기 테스트",
                "설명",
                List.of("카테고리"),
                LocalDate.now(),
                LocalDateTime.now(),
                true,
                testUser.getUuid(),
                Color.RED.getDescription(),
                Icon.DATABASE.getDescription(),
                12
        );

        given(roadmapFacade.toggleFavorite(roadmapId, testUser)).willReturn(fakeResponse);

        mvc.perform(post("/roadmap/{id}/favorite", roadmapId)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void 마지막으로_접속한_로드맵을_조회한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        PersonalRoadmapResponse fakeResponse = new PersonalRoadmapResponse(
                2L,
                "마지막 접속 로드맵",
                "최근 접속한 로드맵 설명",
                List.of("최근", "접속"),
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                testUser.getUuid(),
                Color.ORANGE.getDescription(),
                Icon.DATABASE.getDescription(),
                12
        );

        given(roadmapService.getLastAccessedRoadmap(testUser)).willReturn(fakeResponse);

        mvc.perform(get("/roadmap/last-accessed"))
                .andExpect(status().isOk());
    }

    @Test
    void 로드맵_개수를_조회한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        long expectedCount = 5L;
        RoadmapCountResponse response = new RoadmapCountResponse(expectedCount);
        given(roadmapService.getRoadmapCount(testUser)).willReturn(response);

        mvc.perform(get("/roadmap/count")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void 로드맵을_업데이트한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        Long roadmapId = 1L;
        RoadmapRequest updateRequest = new RoadmapRequest(
                "업데이트 타이틀",
                "업데이트 설명",
                List.of("업데이트 카테고리1", "업데이트 카테고리2"),
                2L,
                Color.BLUE,
                Icon.DATABASE
        );

        PersonalRoadmapResponse fakeResponse = new PersonalRoadmapResponse(
                roadmapId,
                updateRequest.getTitle(),
                updateRequest.getDescription(),
                updateRequest.getCategories(),
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                testUser.getUuid(),
                Color.ORANGE.getDescription(),
                Icon.DATABASE.getDescription(),
                12
        );

        given(roadmapFacade.update(any(Long.class), any(RoadmapRequest.class), any(User.class)))
                .willReturn(fakeResponse);

        mvc.perform(put("/roadmap/{id}", roadmapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void 로드맵을_삭제한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        Long roadmapId = 1L;

        mvc.perform(delete("/roadmap/{id}", roadmapId)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

}
