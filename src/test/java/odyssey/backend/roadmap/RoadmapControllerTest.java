package odyssey.backend.roadmap;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.roadmap.dto.response.RoadmapCountResponse;
import odyssey.backend.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.roadmap.dto.response.RoadmapResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoadmapControllerTest extends RestDocsSupport {

    @WithMockUser
    @Test
    void 로드맵을_생성한다() throws Exception {
        RoadmapRequest request = new RoadmapRequest("자바자바", "조아요", List.of("백엔드", "스프링"), 1L);
        RoadmapResponse fakeResponse = new RoadmapResponse(
                1L,
                request.getTitle(),
                request.getDescription(),
                request.getCategories(),
                "fake-url",
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                "내 로드맵"
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

        given(roadmapFacade.save(any(RoadmapRequest.class), any(MultipartFile.class)))
                .willReturn(fakeResponse);

        mvc.perform(multipart("/roadmap/create")
                        .file(roadmapPart)
                        .file(thumbnail)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(fakeResponse.id()))
                .andExpect(jsonPath("$.data.title").value(fakeResponse.title()))
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
                                fieldWithPath("data.location").description("로드맵 위치 정보")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 로드맵을_전체조회한다() throws Exception {
        RoadmapResponse response1 = new RoadmapResponse(
                1L, "타이틀1", "설명1", List.of("테스트1", "테스트2"),
                "https://image1.com", LocalDate.now(), LocalDateTime.now(), true, "내 로드맵"
        );
        RoadmapResponse response2 = new RoadmapResponse(
                2L, "타이틀2", "설명2", List.of("테스트3", "테스트4"),
                "https://image2.com", LocalDate.now(), LocalDateTime.now(), false, "내 로드맵"
        );

        given(roadmapService.findAllRoadmaps()).willReturn(List.of(response1, response2));

        mvc.perform(get("/roadmap/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(response1.id()))
                .andExpect(jsonPath("$.data[1].id").value(response2.id()))
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
                                fieldWithPath("data[].location").description("로드맵 위치 정보")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 로드맵을_수정한다() throws Exception {
        Long roadmapId = 1L;
        RoadmapRequest request = new RoadmapRequest(
                "수정된 타이틀", "수정된 설명", List.of("수정된 테스트1", "수정된 테스트2"), 1L
        );

        RoadmapResponse fakeResponse = new RoadmapResponse(
                roadmapId,
                request.getTitle(),
                request.getDescription(),
                request.getCategories(),
                "https://updatedThumbnail.com",
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                "내 로드맵"
        );

        given(roadmapFacade.update(eq(roadmapId), any(RoadmapRequest.class)))
                .willReturn(fakeResponse);

        mvc.perform(put("/roadmap/update/{id}", roadmapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(roadmapId))
                .andDo(document("roadmap-update",
                        requestFields(
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
                                fieldWithPath("data.location").description("로드맵 위치 정보")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 로드맵을_삭제한다() throws Exception {
        Long roadmapId = 1L;

        mvc.perform(delete("/roadmap/delete/{id}", roadmapId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("roadmap-delete",
                        pathParameters(
                                parameterWithName("id").description("삭제할 로드맵 ID")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 글자수_제한을_넘은_입력을_입력한다() throws Exception {
        RoadmapRequest request = new RoadmapRequest(
                "자바".repeat(50),
                "조아요",
                List.of("스프링"),
                1L
        );

        mvc.perform(post("/roadmap/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().is4xxClientError())
                .andDo(document("roadmap-create-invalid"));
    }

    @WithMockUser
    @Test
    void 로드맵_즐겨찾기를_토글한다() throws Exception {
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
                "내 로드맵"
        );

        given(roadmapFacade.toggleFavorite(roadmapId)).willReturn(fakeResponse);

        mvc.perform(post("/roadmap/favorite/{id}", roadmapId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(roadmapId))
                .andDo(document("roadmap-toggle-favorite",
                        pathParameters(
                                parameterWithName("id").description("즐겨찾기 토글 대상 로드맵 ID")
                        ),
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
                                fieldWithPath("data.location").description("위치 정보")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 마지막으로_접속한_로드맵을_조회한다() throws Exception {
        RoadmapResponse fakeResponse = new RoadmapResponse(
                2L,
                "마지막 접속 로드맵",
                "최근 접속한 로드맵 설명",
                List.of("최근", "접속"),
                "last.jpg",
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                "내 로드맵"
        );

        given(roadmapService.getLastAccessedRoadmap()).willReturn(fakeResponse);

        mvc.perform(get("/roadmap/last-accessed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(fakeResponse.id()))
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
                                fieldWithPath("data.location").description("위치 정보")
                        )
                ));
    }

    @WithMockUser
    @Test
    void 로드맵_개수를_조회한다() throws Exception {
        long expectedCount = 5L;
        RoadmapCountResponse response = new RoadmapCountResponse(expectedCount);
        given(roadmapService.getRoadmapCount()).willReturn(response);

        mvc.perform(get("/roadmap/count")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(expectedCount))
                .andDo(document("roadmap-get-count",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.count").description("전체 로드맵 개수")
                        )
                ));
    }
}
