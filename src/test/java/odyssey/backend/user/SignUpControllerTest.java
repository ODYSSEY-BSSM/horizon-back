package odyssey.backend.user;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.user.dto.request.SignUpRequest;
import odyssey.backend.user.dto.response.SignUpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SignUpControllerTest extends RestDocsSupport {

    @WithMockUser
    @Test
    void 회원가입을_한다() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("fakeEmail@gmail.com", "이건우", "1234");

        SignUpResponse fakeResponse = new SignUpResponse(1L, signUpRequest.getEmail(), signUpRequest.getUsername());

        given(signUpService.signUp(any(SignUpRequest.class)))
                .willReturn(fakeResponse);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.uuid").value(1L))
                .andExpect(jsonPath("$.data.email").value("fakeEmail@gmail.com"))
                .andDo(document("user-signup",
                        requestFields(
                                fieldWithPath("email").description("회원가입 이메일"),
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.uuid").description("유저 ID"),
                                fieldWithPath("data.email").description("유저 이메일"),
                                fieldWithPath("data.username").description("유저 이름")
                        )
                ));
    }
}
