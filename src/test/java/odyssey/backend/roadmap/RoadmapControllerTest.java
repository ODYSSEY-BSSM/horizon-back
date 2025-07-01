package odyssey.backend.roadmap;

import odyssey.backend.global.ControllerTest;
import odyssey.backend.roadmap.dto.RoadmapCountResponse;
import odyssey.backend.roadmap.dto.RoadmapRequest;
import odyssey.backend.roadmap.dto.RoadmapResponse;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoadmapControllerTest extends ControllerTest {

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

        given(roadmapService.save(any(RoadmapRequest.class), any(MultipartFile.class)))
                .willReturn(fakeResponse);

        mvc.perform(multipart("/roadmap/create")
                        .file(roadmapPart)
                        .file(thumbnail)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(fakeResponse.getId()))
                .andExpect(jsonPath("$.data.title").value(fakeResponse.getTitle()));
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
                .andExpect(jsonPath("$.dataList[0].id").value(response1.getId()))
                .andExpect(jsonPath("$.dataList[1].id").value(response2.getId()));
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

        given(roadmapService.update(eq(roadmapId), any(RoadmapRequest.class)))
                .willReturn(fakeResponse);

        mvc.perform(put("/roadmap/update/{id}", roadmapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(roadmapId));
    }

    @WithMockUser
    @Test
    void 로드맵을_삭제한다() throws Exception {
        Long roadmapId = 1L;

        mvc.perform(delete("/roadmap/delete/{id}", roadmapId)
                        .with(csrf()))
                .andExpect(status().isOk());
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
                .andExpect(status().is4xxClientError());
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

        given(roadmapService.toggleFavorite(roadmapId)).willReturn(fakeResponse);

        mvc.perform(post("/roadmap/favorite/{id}", roadmapId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(roadmapId));
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
                .andExpect(jsonPath("$.data.id").value(fakeResponse.getId()));
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
                .andExpect(jsonPath("$.data.count").value(expectedCount));
    }
}
