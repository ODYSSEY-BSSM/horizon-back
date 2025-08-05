package odyssey.backend.user;

import odyssey.backend.global.ControllerTest;
import odyssey.backend.user.dto.request.SignUpRequest;
import odyssey.backend.user.dto.response.SignUpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SingUpControllerTest extends ControllerTest {

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
                .andExpect(jsonPath("$.data.email").value("fakeEmail@gmail.com"));
    }

}
