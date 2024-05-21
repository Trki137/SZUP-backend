package infsus.szup.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "TEAM",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"TEAM_NAME", "PROJECT_ID"})
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(name = "TEAM_NAME", length = 50, nullable = false)
    private String teamName;

    @JoinColumn(name = "PROJECT_ID", nullable = false)
    @ManyToOne
    private ProjectEntity project;
}
