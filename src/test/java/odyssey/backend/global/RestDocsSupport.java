package odyssey.backend.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public abstract class RestDocsSupport extends ControllerTest{

    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mvc = webAppContextSetup(context)
                .apply(
                        documentationConfiguration(restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint())
                )
                .build();
    }
}
