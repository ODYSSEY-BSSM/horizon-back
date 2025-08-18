package odyssey.backend.global.jwt;

import odyssey.backend.global.RestDocsSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

public class JwtTest extends RestDocsSupport {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void 인증에_성공한다() throws Exception {
        mockMvc.perform(get("/dummy")
                        .header("Authorization", "Bearer thisis.access.token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(document("shared-auth",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer <access_token>")
                        )
                ));

    }

}
