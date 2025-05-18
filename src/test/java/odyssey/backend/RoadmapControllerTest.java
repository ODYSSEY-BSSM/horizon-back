package odyssey.backend;

import odyssey.backend.global.ControllerTest;
import odyssey.backend.roadmap.dto.RoadmapRequest;
import odyssey.backend.roadmap.dto.RoadmapResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoadmapControllerTest extends ControllerTest {

    @WithMockUser
    @Test
    void 로드맵을_생성한다() throws Exception {

        RoadmapRequest request = new RoadmapRequest("자바자바", "조아요");

        RoadmapResponse fakeResponse = new RoadmapResponse(
                1L,
                request.getTitle(),
                request.getDescription()
        );

        given(roadmapService.save(any(RoadmapRequest.class)))
                .willReturn(fakeResponse);

        String responseBody = mvc.perform(post("/roadmap/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("요청 값: " + objectMapper.writeValueAsString(request));
        System.out.println("응답 결과: " + responseBody);
    }

    @WithMockUser
    @Test
    void 로드맵을_전체조회한다() throws Exception {
        RoadmapResponse response1 = new RoadmapResponse(1L, "타이틀1", "설명1");
        RoadmapResponse response2 = new RoadmapResponse(2L, "타이틀2", "설명2");

        given(roadmapService.findAllRoadmaps()).willReturn(List.of(response1, response2));

        String responseBody = mvc.perform(get("/roadmap/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("전체 조회 응답: " + responseBody);
    }

    @WithMockUser
    @Test
    void 로드맵을_수정한다() throws Exception {
        Long roadmapId = 1L;
        RoadmapRequest request = new RoadmapRequest("수정된 타이틀", "수정된 설명");
        RoadmapResponse fakeResponse = new RoadmapResponse(roadmapId, request.getTitle(), request.getDescription());

        given(roadmapService.update(eq(roadmapId), any(RoadmapRequest.class))).willReturn(fakeResponse);

        String responseBody = mvc.perform(put("/roadmap/update/{id}", roadmapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("수정 요청: " + objectMapper.writeValueAsString(request));
        System.out.println("수정 응답: " + responseBody);
    }

    @WithMockUser
    @Test
    void 로드맵을_삭제한다() throws Exception {
        Long roadmapId = 1L;

        mvc.perform(delete("/roadmap/delete/{id}", roadmapId)
                        .with(csrf()))
                .andExpect(status().isOk());

        System.out.println("삭제 요청: id=" + roadmapId);
    }
}
