package odyssey.backend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static User from(SignUpRequest request, String password){
        return new User(request.getEmail(), request.getUsername(), password);
    }

    User(String email, String username, String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
