package infsus.szup.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRIORITY")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriorityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRIORITY_ID")
    private Integer id;

    @Column(name = "LEVEL", unique = true, nullable = false)
    private Short level;

    @Column(name = "PRIORITY_NAME", unique = true, nullable = false)
    private String priorityName;
}
