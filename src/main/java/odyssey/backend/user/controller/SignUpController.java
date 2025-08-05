package odyssey.backend.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import odyssey.backend.user.dto.request.SignUpRequest;
import odyssey.backend.user.dto.response.SignUpResponse;
import odyssey.backend.user.service.SignUpService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return CommonResponse.ok(signUpService.signUp(signUpRequest));
    }
}
