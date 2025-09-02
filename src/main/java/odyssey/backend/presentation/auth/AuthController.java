package odyssey.backend.presentation.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import odyssey.backend.application.auth.*;
import odyssey.backend.infrastructure.jwt.dto.response.TokenResponse;
import odyssey.backend.shared.response.CommonResponse;
import odyssey.backend.shared.response.SingleCommonResponse;
import odyssey.backend.domain.auth.User;
import odyssey.backend.presentation.auth.dto.request.LoginRequest;
import odyssey.backend.presentation.auth.dto.request.SignUpRequest;
import odyssey.backend.presentation.auth.dto.response.SignUpResponse;
import odyssey.backend.presentation.auth.dto.response.UserResponse;
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
    private final GetUserInfoService getUserInfoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleCommonResponse<SignUpResponse> signUp(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        return CommonResponse.ok(signUpService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SingleCommonResponse<TokenResponse> login(
            @RequestBody @Valid LoginRequest request
    ){
        return CommonResponse.ok(loginService.login(request));
    }

    @GetMapping("/refresh")
    public SingleCommonResponse<TokenResponse> refreshAccessToken(
            @RequestHeader("Refresh-Token") String refreshToken
    ) {
        return CommonResponse.ok(refreshService.refreshToken(refreshToken));
    }

    @GetMapping("/logout")
    public SingleCommonResponse<String> logout(
            @AuthenticationPrincipal User user
    ) {
        logoutService.logout(user);
        return CommonResponse.ok("로그아웃되었습니다.");
    }

    @GetMapping("/info")
    public SingleCommonResponse<UserResponse> info(
            @AuthenticationPrincipal User user
    ) {
        return CommonResponse.ok(getUserInfoService.getUserInfo(user));
    }
}
