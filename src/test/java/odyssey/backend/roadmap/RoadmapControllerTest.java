package odyssey.backend.roadmap;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.global.test.UserCreate;
import odyssey.backend.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.roadmap.dto.response.RoadmapCountResponse;
import odyssey.backend.roadmap.dto.response.RoadmapResponse;
import odyssey.backend.user.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoadmapControllerTest extends RestDocsSupport {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void 로드맵을_생성한다() throws Exception {
        RoadmapRequest request = new RoadmapRequest("자바자바", "조아요", List.of("백엔드", "스프링"), 1L);
        User testUser = UserCreate.createUser();

        RoadmapResponse fakeResponse = new RoadmapResponse(
                1L,
                request.getTitle(),
                request.getDescription(),
                request.getCategories(),
                "fake-url",
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                "내 로드맵",
                testUser.getUuid()
        );

        MockMultipartFile roadmapPart = new MockMultipartFile(
                "roadmap",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)
        );

        MockMultipartFile thumbnail = new MockMultipartFile(
                "thumbnail",
                "image.jpg",
                "image/jpeg",
                "<<fake image content>>".getBytes()
        );

        given(roadmapFacade.save(any(RoadmapRequest.class), any(MultipartFile.class), any(User.class)))
                .willReturn(fakeResponse);

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        mvc.perform(multipart("/roadmap/create")
                        .file(roadmapPart)
                        .file(thumbnail)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(document("roadmap-create",
                        requestPartFields("roadmap",
                                fieldWithPath("title").description("로드맵 제목"),
                                fieldWithPath("description").description("로드맵 설명"),
                                fieldWithPath("categories").description("카테고리 리스트"),
                                fieldWithPath("directoryId").description("디렉토리 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("로드맵 ID"),
                                fieldWithPath("data.title").description("로드맵 제목"),
                                fieldWithPath("data.description").description("로드맵 설명"),
                                fieldWithPath("data.categories").description("카테고리 리스트"),
                                fieldWithPath("data.thumbnailUrl").description("썸네일 URL"),
                                fieldWithPath("data.lastModifiedAt").description("마지막 수정 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("data.lastAccessedAt").description("마지막 접속 일시 (yyyy-MM-ddTHH:mm:ss)"),
                                fieldWithPath("data.isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data.location").description("로드맵 위치 정보"),
                                fieldWithPath("data.uuid").description("유저 id")
                        )
                ));
    }

    @Test
    void 로드맵을_전체조회한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        RoadmapResponse response1 = new RoadmapResponse(
                1L, "타이틀1", "설명1", List.of("테스트1", "테스트2"),
                "https://image1.com", LocalDate.now(), LocalDateTime.now(), true, "내 로드맵",
                testUser.getUuid()
        );

        RoadmapResponse response2 = new RoadmapResponse(
                2L, "타이틀2", "설명2", List.of("테스트3", "테스트4"),
                "https://image2.com", LocalDate.now(), LocalDateTime.now(), false, "내 로드맵",
                testUser.getUuid()
        );

        given(roadmapService.findAllRoadmaps(any(User.class)))
                .willReturn(List.of(response1, response2));

        mvc.perform(get("/roadmap/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("roadmap-get-all",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("로드맵 ID"),
                                fieldWithPath("data[].title").description("로드맵 제목"),
                                fieldWithPath("data[].description").description("로드맵 설명"),
                                fieldWithPath("data[].categories").description("카테고리 리스트"),
                                fieldWithPath("data[].thumbnailUrl").description("썸네일 URL"),
                                fieldWithPath("data[].lastModifiedAt").description("마지막 수정 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("data[].lastAccessedAt").description("마지막 접속 일시 (yyyy-MM-ddTHH:mm:ss)"),
                                fieldWithPath("data[].isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data[].location").description("로드맵 위치 정보"),
                                fieldWithPath("data[].uuid").description("유저 id")
                        )
                ));
    }

    @Test
    void 로드맵_즐겨찾기를_토글한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        Long roadmapId = 1L;
        RoadmapResponse fakeResponse = new RoadmapResponse(
                roadmapId,
                "즐겨찾기 테스트",
                "설명",
                List.of("카테고리"),
                "https://thumbnail.com/image.jpg",
                LocalDate.now(),
                LocalDateTime.now(),
                true,
                "내 로드맵",
                testUser.getUuid()
        );

        given(roadmapFacade.toggleFavorite(roadmapId, testUser)).willReturn(fakeResponse);

        mvc.perform(post("/roadmap/favorite/{id}", roadmapId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("roadmap-toggle-favorite",
                        pathParameters(
                                parameterWithName("id").description("즐겨찾기 토글 대상 로드맵 ID")
                        )
                ));
    }

    @Test
    void 마지막으로_접속한_로드맵을_조회한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        RoadmapResponse fakeResponse = new RoadmapResponse(
                2L,
                "마지막 접속 로드맵",
                "최근 접속한 로드맵 설명",
                List.of("최근", "접속"),
                "last.jpg",
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                "내 로드맵",
                testUser.getUuid()
        );

        given(roadmapService.getLastAccessedRoadmap(testUser)).willReturn(fakeResponse);

        mvc.perform(get("/roadmap/last-accessed")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("roadmap-get-last-accessed",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("로드맵 ID"),
                                fieldWithPath("data.title").description("로드맵 제목"),
                                fieldWithPath("data.description").description("설명"),
                                fieldWithPath("data.categories").description("카테고리 리스트"),
                                fieldWithPath("data.thumbnailUrl").description("썸네일 URL"),
                                fieldWithPath("data.lastModifiedAt").description("마지막 수정 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("data.lastAccessedAt").description("마지막 접속 일시 (yyyy-MM-ddTHH:mm:ss)"),
                                fieldWithPath("data.isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data.location").description("위치 정보"),
                                fieldWithPath("data.uuid").description("유저 id")
                        )
                ));
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
                .andExpect(status().isOk())
                .andDo(document("roadmap-get-count",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.count").description("전체 로드맵 개수")
                        )
                ));
    }
}
