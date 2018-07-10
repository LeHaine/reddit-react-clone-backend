package clone.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by colt on 7/8/18.
 */
@Entity
@Table(name = "post")
@Getter @Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Post extends AuditModel {

    @Id
    @GeneratedValue(generator = "post_generator")
    @SequenceGenerator(name = "post_generator", sequenceName = "post_seq", initialValue = 1000)
    private Long id;

    @NotBlank
    @Size(max = 256)
    private String title;

    @NotBlank
    private String type;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sub_id")
    private Sub sub;

    private String content;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<Vote> votes;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Transient
    private long grossVotes;
}
