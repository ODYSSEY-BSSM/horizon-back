package odyssey.backend.domain.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Random;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "signup-verification", timeToLive = 60 * 10)
public class SignUpVerification {

    @Id
    private String email;

    private String code;

    private boolean isVerified;

    public SignUpVerification(String email){
        this.email = email;
        this.code = generateCode();
        this.isVerified = false;
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void verify(){
        isVerified = true;
    }

}
