package odyssey.backend.global.test;
import odyssey.backend.user.domain.Role;
import odyssey.backend.user.domain.User;
import odyssey.backend.user.dto.request.SignUpRequest;

public class UserCreate {
    public static User createUser() {
        SignUpRequest request = new SignUpRequest("leegunwoo0325@gmail.com", "이건우웅", "1234");
        return User.from(request, request.getPassword(), Role.USER);
    }
}
