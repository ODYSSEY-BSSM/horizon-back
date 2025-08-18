package odyssey.backend;

import odyssey.backend.global.jwt.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

    @MockBean
    private TokenService tokenService;


    @Test
    void contextLoads() {
    }

}
