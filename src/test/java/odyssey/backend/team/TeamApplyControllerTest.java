package odyssey.backend.team;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.domain.team.TeamApply;
import odyssey.backend.presentation.team.dto.response.ApplyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TeamApplyControllerTest extends RestDocsSupport {

    @Test
    void 팀에_신청한다() throws Exception {
        ApplyResponse response = new ApplyResponse(1L, "이건우팀", TeamApply.Status.SUBMITTED);
        given(teamApplyService.apply(eq(1L), any())).willReturn(response);

        mvc.perform(post("/apply/{teamId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.applyStatus").value(TeamApply.Status.SUBMITTED.name()))
                .andDo(document("team-apply",
                        pathParameters(
                                parameterWithName("teamId").description("팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("신청 ID"),
                                fieldWithPath("data.teamName").description("팀 이름"),
                                fieldWithPath("data.applyStatus").description("승인 여부")
                        )
                ));
    }

    @Test
    void 팀_신청을_승인한다() throws Exception {
        ApplyResponse response = new ApplyResponse(1L, "이건우팀", TeamApply.Status.SUBMITTED);
        given(teamApplyService.approve(eq(1L), any())).willReturn(response);

        mvc.perform(put("/apply/{applyId}/approve", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.applyStatus").value(TeamApply.Status.SUBMITTED.name()))
                .andDo(document("team-apply-approve",
                        pathParameters(
                                parameterWithName("applyId").description("신청 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("신청 ID"),
                                fieldWithPath("data.teamName").description("팀 이름"),
                                fieldWithPath("data.applyStatus").description("승인 여부")
                        )
                ));
    }

    @Test
    void 팀_신청을_거절한다() throws Exception {

        mvc.perform(patch("/apply/{applyId}/reject", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("삭제되었습니다."))
                .andDo(document("team-apply-reject",
                        pathParameters(
                                parameterWithName("applyId").description("신청 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("삭제 메시지")
                        )
                ));
    }
}
