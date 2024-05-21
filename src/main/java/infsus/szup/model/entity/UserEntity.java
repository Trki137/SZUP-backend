package infsus.szup.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME", length = 35, nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", length = 45, nullable = false)
    private String lastName;

    @Column(name = "EMAIL", unique = true, length = 120, nullable = false)
    private String email;

    @Column(name = "PASSWORD", length = 64, nullable = false)
    private String password;
}
