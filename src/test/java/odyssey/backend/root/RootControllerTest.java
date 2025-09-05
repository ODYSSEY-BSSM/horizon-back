package odyssey.backend.root;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.presentation.directory.dto.response.DirectoryResponse;
import odyssey.backend.presentation.roadmap.dto.response.SimpleRoadmapResponse;
import odyssey.backend.presentation.root.dto.response.RootContentResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RootControllerTest extends RestDocsSupport {

    @Test
    void 루트_컨텐츠를_조회한다() throws Exception {
        DirectoryResponse directory = new DirectoryResponse(1L, "루트 디렉토리", null, List.of(), List.of());
        SimpleRoadmapResponse roadmap = new SimpleRoadmapResponse(1L, "루트 로드맵");
        RootContentResponse response = new RootContentResponse(List.of(directory), List.of(roadmap));

        given(rootUseCase.getRootContents(any()))
                .willReturn(response);

        mvc.perform(get("/root"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.directories[0].id").value(1L))
                .andExpect(jsonPath("$.data.directories[0].name").value("루트 디렉토리"))
                .andExpect(jsonPath("$.data.roadmaps[0].id").value(1L))
                .andExpect(jsonPath("$.data.roadmaps[0].title").value("루트 로드맵"))
                .andDo(document("directory-root-contents",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.directories[].id").description("디렉토리 ID"),
                                fieldWithPath("data.directories[].name").description("디렉토리 이름"),
                                fieldWithPath("data.directories[].parentId").description("상위 디렉토리 ID").optional(),
                                fieldWithPath("data.directories[].directories").description("하위 디렉토리 리스트"),
                                fieldWithPath("data.directories[].roadmaps").description("디렉토리에 포함된 로드맵 리스트"),
                                fieldWithPath("data.roadmaps[].id").description("로드맵 ID"),
                                fieldWithPath("data.roadmaps[].title").description("로드맵 제목")
                        )
                ));

    }
}
