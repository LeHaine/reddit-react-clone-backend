package clone.reddit.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by colt on 7/9/18.
 */
@Entity
@Table(name = "sub")
@Getter
@Setter
public class Sub extends AuditModel {

    @Id
    @GeneratedValue(generator = "sub_generator")
    @SequenceGenerator(name = "sub_generator", sequenceName = "sub_seq", initialValue = 1000)
    private Long id;

    @Column(unique = true)
    @NotBlank
    @Size(max = 30)
    private String name;


    @OneToMany(mappedBy = "sub")
    private List<Post> posts;

    @JoinColumn(name = "owner_id")
    private Account owner;


}
