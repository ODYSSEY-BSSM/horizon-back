package odyssey.backend.team;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.team.dto.request.TeamRequest;
import odyssey.backend.team.dto.response.TeamResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamControllerTest extends RestDocsSupport {

    @Test
    public void 팀을_생성한다() throws Exception {
        Long teamId = 1L;
        TeamRequest teamRequest = new TeamRequest("이건우 화이팅");

        TeamResponse fakeResponse = new TeamResponse(1L, "이건우화이팅", "이건우", List.of("이건우", "삼건우"));

        given(teamService.create(any(TeamRequest.class), any()))
                .willReturn(fakeResponse);

        mvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-create",
                        requestFields(
                                fieldWithPath("name").description("팀 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("팀 ID"),
                                fieldWithPath("data.name").description("팀 이름"),
                                fieldWithPath("data.leader").description("팀장 이름"),
                                fieldWithPath("data.members").description("팀 멤버")
                        )
                ));
    }

    @Test
    void 팀을_삭제한다() throws Exception {
        Long teamId = 1L;

        mvc.perform(delete("/teams/{id}", teamId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("team-delete",
                        pathParameters(
                                parameterWithName("id").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("삭제 결과 메시지")
                        )
                ));
    }

    @Test
    void 팀을_조회한다() throws Exception {
        Long teamId = 1L;
        TeamResponse fakeResponse = new TeamResponse(1L, "이건우화이팅", "이건우", List.of("이건우", "삼건우"));
        given(teamService.findById(teamId)).willReturn(fakeResponse);

        mvc.perform(get("/teams/{teamId}", teamId))
                .andExpect(status().isOk())
                .andDo(document("team-get",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("팀 ID"),
                                fieldWithPath("data.name").description("팀 이름"),
                                fieldWithPath("data.leader").description("팀장 이름"),
                                fieldWithPath("data.members").description("팀 멤버")
                        )
                ));
    }
}
