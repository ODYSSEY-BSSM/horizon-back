package odyssey.backend.shared.test;
import odyssey.backend.domain.auth.Role;
import odyssey.backend.domain.auth.User;
import odyssey.backend.presentation.auth.dto.request.SignUpRequest;

public class UserCreate {
    public static User createUser() {
        SignUpRequest request = new SignUpRequest("leegunwoo0325@gmail.com", "이건우웅", "1234");
        return User.from(request, request.getPassword(), Role.USER);
    }
}
