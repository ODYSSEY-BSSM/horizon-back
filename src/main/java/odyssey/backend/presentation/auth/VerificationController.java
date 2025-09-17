package odyssey.backend.presentation.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.application.auth.SendVerificationCodeService;
import odyssey.backend.application.auth.VerificationValidUseCase;
import odyssey.backend.presentation.auth.dto.request.SendVerificationRequest;
import odyssey.backend.presentation.auth.dto.request.VerifyRequest;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/verification")
public class VerificationController {

    private final SendVerificationCodeService sendVerificationCodeService;
    private final VerificationValidUseCase verificationValidUseCase;

    @PostMapping
    public void sendVerificationCode(
            @Valid @RequestBody SendVerificationRequest request
    ) {
        sendVerificationCodeService.sendVerificationCode(request);
    }

    @PatchMapping
    public void verification(
            @Valid @RequestBody VerifyRequest request
    ){
        verificationValidUseCase.verify(request);
    }

}
