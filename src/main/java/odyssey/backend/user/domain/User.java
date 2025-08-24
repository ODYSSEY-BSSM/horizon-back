package odyssey.backend.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import odyssey.backend.team.domain.Team;
import odyssey.backend.user.dto.request.SignUpRequest;

import java.util.List;

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

    private String password;

    private Role role;

    @ManyToMany(mappedBy = "members")
    private List<Team> teams;

    public static User from(SignUpRequest request, String password, Role role) {
        return new User(request.getEmail(), request.getUsername(), password, role);
    }

    public User(String email, String username, String password, Role role){
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

}


