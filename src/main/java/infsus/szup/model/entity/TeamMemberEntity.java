package infsus.szup.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "TEAM_MEMBER",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"USER_ID", "TEAM_ID"})
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_MEMBER_ID")
    private Long id;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne
    private UserEntity teamMember;

    @JoinColumn(name = "TEAM_ID", nullable = false)
    @ManyToOne
    private TeamEntity team;

    @Column(name = "TEAM_LEADER", nullable = false)
    private Boolean teamLeader;

}
