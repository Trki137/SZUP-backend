package infsus.szup.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASK")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TASK_ID")
    private Long id;

    @Column(name = "TASK_NAME", length = 50, nullable = false)
    private String taskName;

    @Column(name = "TASK_SET_DATE", nullable = false)
    private LocalDateTime taskSetDate;

    @Column(name = "TASK_END_DATE")
    private LocalDateTime taskEndDate;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "NUMBER_OF_HOURS")
    private Short numberOfHours;

    @JoinColumn(name = "PROJECT_ID",nullable = false)
    @ManyToOne
    private ProjectEntity project;

    @JoinColumn(name = "TASK_OWNER_ID",nullable = false)
    @ManyToOne
    private UserEntity taskOwner;

    @JoinColumn(name = "TASK_SOLVER_ID",nullable = false)
    @ManyToOne
    private UserEntity taskSolver;

    @JoinColumn(name = "STATUS_ID", nullable = false)
    @ManyToOne
    private StatusEntity currentStatus;

    @JoinColumn(name = "PRIORITY_ID", nullable = false)
    @ManyToOne
    private PriorityEntity priority;
}
