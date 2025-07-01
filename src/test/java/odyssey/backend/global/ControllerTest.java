package odyssey.backend.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import odyssey.backend.directory.controller.DirectoryController;
import odyssey.backend.directory.service.DirectoryService;
import odyssey.backend.node.controller.NodeController;
import odyssey.backend.node.service.NodeService;
import odyssey.backend.roadmap.controller.RoadmapController;
import odyssey.backend.roadmap.service.RoadmapService;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@WebMvcTest({RoadmapController.class,
             NodeController.class,
             DirectoryController.class,})
public class ControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected RoadmapService roadmapService;

    @MockBean
    protected NodeService nodeService;

    @MockBean
    protected DirectoryService directoryService;

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
