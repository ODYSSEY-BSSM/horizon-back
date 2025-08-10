package odyssey.backend.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.user.dto.request.SignUpRequest;

@Entity
@Table(name = "user_tbl")
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private Role role;

    public static User from(SignUpRequest request, String password, Role role) {
        return new User(request.getEmail(), request.getUsername(), password, role);
    }

    User(String email, String username, String password, Role role){
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

}


