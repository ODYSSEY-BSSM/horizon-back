package odyssey.backend.problem;

import odyssey.backend.domain.auth.User;
import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.presentation.problem.dto.request.ProblemRequest;
import odyssey.backend.presentation.problem.dto.request.SolveProblemRequest;
import odyssey.backend.presentation.problem.dto.response.ProblemResponse;
import odyssey.backend.shared.test.UserCreate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProblemControllerTest extends RestDocsSupport {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void 문제를_생성한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        ProblemRequest request = new ProblemRequest("테스트 문제", "정답");

        ProblemResponse fakeResponse = new ProblemResponse(
                1L,
                "테스트 문제",
                null
        );

        given(problemService.createProblem(any(ProblemRequest.class), any(Long.class)))
                .willReturn(fakeResponse);

        mvc.perform(post("/1/problems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("problem-create",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("문제 ID"),
                                fieldWithPath("data.title").description("문제 제목"),
                                fieldWithPath("data.status").description("문제 상태")
                        )
                ));
    }

    @Test
    void 문제를_풀이한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        SolveProblemRequest request = new SolveProblemRequest("정답");

        ProblemResponse fakeResponse = new ProblemResponse(
                1L,
                "테스트 문제",
                null
        );

        given(problemService.solveProblem(any(Long.class), any(SolveProblemRequest.class)))
                .willReturn(fakeResponse);

        mvc.perform(patch("/1/problems")
                        .param("problemId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("problem-solve",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("문제 ID"),
                                fieldWithPath("data.title").description("문제 제목"),
                                fieldWithPath("data.status").description("문제 상태")
                        )
                ));
    }

}
