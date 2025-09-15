package odyssey.backend.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import odyssey.backend.application.auth.*;
import odyssey.backend.application.directory.DirectoryService;
import odyssey.backend.application.node.NodeService;
import odyssey.backend.application.problem.ProblemService;
import odyssey.backend.application.roadmap.RoadmapFacade;
import odyssey.backend.application.roadmap.RoadmapService;
import odyssey.backend.application.root.RootUseCase;
import odyssey.backend.application.team.TeamApplyService;
import odyssey.backend.application.team.TeamService;
import odyssey.backend.domain.auth.User;
import odyssey.backend.infrastructure.jwt.service.TokenService;
import odyssey.backend.presentation.auth.AuthController;
import odyssey.backend.presentation.directory.DirectoryController;
import odyssey.backend.presentation.node.NodeController;
import odyssey.backend.presentation.problem.ProblemController;
import odyssey.backend.presentation.roadmap.RoadmapController;
import odyssey.backend.presentation.roadmap.TeamRoadmapController;
import odyssey.backend.presentation.root.RootController;
import odyssey.backend.presentation.team.TeamApplyController;
import odyssey.backend.presentation.team.TeamController;
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
             TeamApplyController.class,
             RootController.class,
             TeamRoadmapController.class,
             ProblemController.class,})
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

    @MockBean
    protected GetUserInfoService getUserInfoService;

    @MockBean
    protected RootUseCase rootUseCase;

    @MockBean
    protected ProblemService problemService;

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
