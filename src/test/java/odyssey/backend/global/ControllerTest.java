package odyssey.backend.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import odyssey.backend.directory.controller.DirectoryController;
import odyssey.backend.directory.service.DirectoryService;
import odyssey.backend.global.jwt.service.TokenService;
import odyssey.backend.node.controller.NodeController;
import odyssey.backend.node.service.NodeService;
import odyssey.backend.roadmap.controller.RoadmapController;
import odyssey.backend.roadmap.service.RoadmapFacade;
import odyssey.backend.roadmap.service.RoadmapService;
import odyssey.backend.team.controller.TeamApplyController;
import odyssey.backend.team.controller.TeamController;
import odyssey.backend.team.service.TeamApplyService;
import odyssey.backend.team.service.TeamService;
import odyssey.backend.user.controller.AuthController;
import odyssey.backend.user.domain.User;
import odyssey.backend.user.service.LoginService;
import odyssey.backend.user.service.LogoutService;
import odyssey.backend.user.service.RefreshService;
import odyssey.backend.user.service.SignUpService;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@Disabled
@WebMvcTest({RoadmapController.class,
             NodeController.class,
             DirectoryController.class,
             AuthController.class,
             TeamController.class,
             TeamApplyController.class,})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected RoadmapService roadmapService;

    @MockBean
    protected RoadmapFacade roadmapFacade;

    @MockBean
    protected NodeService nodeService;

    @MockBean
    protected DirectoryService directoryService;

    @MockBean
    protected SignUpService signUpService;

    @MockBean
    protected TokenService tokenService;

    @MockBean
    protected LoginService loginService;

    @MockBean
    protected LogoutService logoutService;

    @MockBean
    protected RefreshService refreshService;

    @MockBean
    protected TeamService teamService;

    @MockBean
    protected TeamApplyService teamApplyService;

    public static RequestPostProcessor authenticationPrincipal(final User user) {
        return new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setAttribute("org.springframework.security.core.annotation.AuthenticationPrincipal", user);
                return request;
            }
        };
    }

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
