package infsus.szup.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ATTACHMENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTACHMENT_ID")
    private Long id;

    @Column(name = "ATTACHMENT_NAME", length = 60, nullable = false)
    private String attachmentName;

    @Column(name = "ATTACHMENT_TYPE", length = 15, nullable = false)
    private String attachmentType;

    @Lob
    @Column(name = "ATTACHMENT", nullable = false)
    private byte[] attachment;

    @JoinColumn(name = "TASK_ID")
    @ManyToOne
    private TaskEntity task;

    @JoinColumn(name = "COMMENT_ID")
    @ManyToOne
    private CommentEntity comment;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne
    private UserEntity attachmentOwner;




}
