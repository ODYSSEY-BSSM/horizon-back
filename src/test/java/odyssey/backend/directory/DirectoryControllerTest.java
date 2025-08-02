package odyssey.backend.directory;

import odyssey.backend.directory.dto.request.DirectoryRequest;
import odyssey.backend.directory.dto.response.DirectoryResponse;
import odyssey.backend.directory.dto.response.RootContentResponse;
import odyssey.backend.global.ControllerTest;
import odyssey.backend.roadmap.dto.response.SimpleRoadmapResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DirectoryControllerTest extends ControllerTest {

    @WithMockUser
    @Test
    void 디렉토리를_생성한다() throws Exception {
        DirectoryRequest request = new DirectoryRequest("새 디렉토리", null);
        DirectoryResponse response = new DirectoryResponse(1L, "새 디렉토리", null, List.of(), List.of());

        given(directoryService.createDirectory(any(DirectoryRequest.class)))
                .willReturn(response);

        mvc.perform(post("/directories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("새 디렉토리"));
    }

    @WithMockUser
    @Test
    void 디렉토리를_수정한다() throws Exception {
        Long id = 1L;
        DirectoryRequest request = new DirectoryRequest("수정된 디렉토리", null);
        DirectoryResponse response = new DirectoryResponse(id, "수정된 디렉토리", null, List.of(), List.of());

        given(directoryService.updateDirectory(eq(id), any(DirectoryRequest.class)))
                .willReturn(response);

        mvc.perform(put("/directories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.name").value("수정된 디렉토리"));
    }

    @WithMockUser
    @Test
    void 디렉토리를_삭제한다() throws Exception {
        Long id = 1L;

        mvc.perform(delete("/directories/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Test
    void 루트_컨텐츠를_조회한다() throws Exception {
        DirectoryResponse directory = new DirectoryResponse(1L, "루트 디렉토리", null, List.of(), List.of());
        SimpleRoadmapResponse roadmap = new SimpleRoadmapResponse(1L, "루트 로드맵");
        RootContentResponse response = new RootContentResponse(List.of(directory), List.of(roadmap));

        given(directoryService.getRootContents())
                .willReturn(response);

        mvc.perform(get("/directories/root-contents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.directories[0].id").value(1L))
                .andExpect(jsonPath("$.data.directories[0].name").value("루트 디렉토리"))
                .andExpect(jsonPath("$.data.roadmaps[0].id").value(1L))
                .andExpect(jsonPath("$.data.roadmaps[0].title").value("루트 로드맵"));
    }
}
