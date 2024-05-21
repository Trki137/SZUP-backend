package infsus.szup.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "COMMENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "POST_TIME", nullable = false)
    private LocalDateTime postTime;

    @Column(name = "TEXT", length = 2048)
    private String text;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne
    private UserEntity writtenBy;

    @JoinColumn(name = "TASK_ID", nullable = false)
    @ManyToOne
    private TaskEntity task;





}
