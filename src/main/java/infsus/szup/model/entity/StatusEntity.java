package infsus.szup.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "STATUS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATUS_ID")
    private Integer id;

    @Column(name = "STAUS_NAME", unique = true, nullable = false)
    private String statusName;
}
