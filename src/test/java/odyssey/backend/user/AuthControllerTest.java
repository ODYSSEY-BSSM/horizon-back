package odyssey.backend.user;

import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.global.jwt.dto.response.TokenResponse;
import odyssey.backend.user.domain.Role;
import odyssey.backend.user.dto.request.LoginRequest;
import odyssey.backend.user.dto.request.SignUpRequest;
import odyssey.backend.user.dto.response.SignUpResponse;
import odyssey.backend.user.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends RestDocsSupport {

    @Test
    void 회원가입을_한다() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("fakeEmail@gmail.com", "이건우", "1234");

        SignUpResponse fakeResponse = new SignUpResponse(1L, signUpRequest.getEmail(), signUpRequest.getUsername(), Role.USER);

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
                                fieldWithPath("data.username").description("유저 이름"),
                                fieldWithPath("data.role").description("권한")
                        )
                ));
    }

    @Test
    void 로그인을_하면_액세스_토큰이_발급된다() throws Exception {
        LoginRequest request = new LoginRequest("fake@Email.com", "fakePassword");

        TokenResponse fakeTokenResponse = TokenResponse.create("fakeAccessToken", "fakeRefreshToken");

        given(loginService.login(any(LoginRequest.class)))
                .willReturn(fakeTokenResponse);

        mvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("fakeAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("fakeRefreshToken"))
                .andDo(document("user-login",
                        requestFields(
                                fieldWithPath("email").description("로그인 이메일"),
                                fieldWithPath("password").description("로그인 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.accessToken").description("발급된 액세스 토큰"),
                                fieldWithPath("data.refreshToken").description("발급된 리프레시 토큰")
                        )
                ));
    }

    @Test
    void 리프레시_토큰으로_액세스_토큰을_재발급한다() throws Exception {
        String fakeRefreshToken = "리프레시토큰 헤더에 넣어주세요";
        TokenResponse fakeTokenResponse = TokenResponse.create("newAccessToken", fakeRefreshToken);

        given(refreshService.refreshToken(fakeRefreshToken))
                .willReturn(fakeTokenResponse);

        mvc.perform(get("/users/refresh")
                        .header("Refresh-Token", fakeRefreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value(fakeRefreshToken))
                .andDo(document("user-refresh",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.accessToken").description("새로 발급된 액세스 토큰"),
                                fieldWithPath("data.refreshToken").description("기존 리프레시 토큰")
                        )
                ));
    }

    @Test
    void 로그아웃_한다() throws Exception {
        String fakeAccessToken = "Bearer 액세스토큰 넣어주세요";

        willDoNothing().given(logoutService).logout(any());

        mvc.perform(get("/users/logout")
                        .header("Authorization", fakeAccessToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("로그아웃되었습니다."))
                .andDo(document("user-logout",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("로그아웃 메시지")
                        )
                ));
    }

    @Test
    void 유저_정보를_가져온다() throws Exception {
        UserResponse fakeResponse = new UserResponse(
                "gunwoo",
                "fakeEmail@gmail.com",
                Role.USER.name(),
                List.of("팀1", "팀2")
        );

        given(getUserInfoService.getUserInfo(any()))
                .willReturn(fakeResponse);

        mvc.perform(get("/users/info")
                        .header("Authorization", "Bearer fakeAccessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("gunwoo"))
                .andExpect(jsonPath("$.data.email").value("fakeEmail@gmail.com"))
                .andExpect(jsonPath("$.data.role").value("USER"))
                .andExpect(jsonPath("$.data.teams[0]").value("팀1"))
                .andDo(document("user-me",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.username").description("사용자 이름"),
                                fieldWithPath("data.email").description("사용자 이메일"),
                                fieldWithPath("data.role").description("사용자 권한"),
                                fieldWithPath("data.teams").description("사용자가 속한 팀 목록")
                        )
                ));
    }


}
