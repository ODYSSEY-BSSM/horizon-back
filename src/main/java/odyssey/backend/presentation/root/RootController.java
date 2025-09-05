package odyssey.backend.presentation.root;

import lombok.RequiredArgsConstructor;
import odyssey.backend.application.root.RootUseCase;
import odyssey.backend.domain.auth.User;
import odyssey.backend.presentation.root.dto.response.RootContentResponse;
import odyssey.backend.shared.response.CommonResponse;
import odyssey.backend.shared.response.SingleCommonResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/root")
public class RootController {

    private final RootUseCase rootUseCase;

    @GetMapping
    public SingleCommonResponse<RootContentResponse> getContent(
            @AuthenticationPrincipal User user
    ){
        return CommonResponse.ok(rootUseCase.getRootContents(user));
    }
}
