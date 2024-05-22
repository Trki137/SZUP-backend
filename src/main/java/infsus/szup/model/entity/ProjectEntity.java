package infsus.szup.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PROJECT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(name = "PROJECT_NAME", unique = true, nullable = false)
    private String projectName;

    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne
    private UserEntity createdBy;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamEntity> teams;
}
