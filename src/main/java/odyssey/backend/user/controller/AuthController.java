package odyssey.backend.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.jwt.dto.response.TokenResponse;
import odyssey.backend.global.response.CommonResponse;
import odyssey.backend.global.response.SingleCommonResponse;
import odyssey.backend.user.domain.User;
import odyssey.backend.user.dto.request.LoginRequest;
import odyssey.backend.user.dto.request.SignUpRequest;
import odyssey.backend.user.dto.response.SignUpResponse;
import odyssey.backend.user.service.LoginService;
import odyssey.backend.user.service.LogoutService;
import odyssey.backend.user.service.RefreshService;
import odyssey.backend.user.service.SignUpService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final SignUpService signUpService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final RefreshService refreshService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return CommonResponse.ok(signUpService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<TokenResponse> login(@RequestBody @Valid LoginRequest request){
        return CommonResponse.ok(loginService.login(request));
    }

    @GetMapping("/refresh")
    public SingleCommonResponse<TokenResponse> refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return CommonResponse.ok(refreshService.refreshToken(refreshToken));
    }

    @GetMapping("/logout")
    public SingleCommonResponse<String> logout(@AuthenticationPrincipal User user) {
        logoutService.logout(user);
        return CommonResponse.ok("로그아웃되었습니다.");
    }
}
