package odyssey.backend.infrastructure.jwt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    private Long uuid;

    private String refreshToken;

    RefreshToken(Long uuid, String refreshToken) {
        this.uuid = uuid;
        this.refreshToken = refreshToken;
    }

    public static RefreshToken create(Long uuid, String refreshToken){
        return new RefreshToken(uuid, refreshToken);
    }
}
