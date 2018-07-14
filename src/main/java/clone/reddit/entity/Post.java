package clone.reddit.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by colt on 7/8/18.
 */
@Entity
@Table(name = "post")
@Getter
@Setter
public class Post extends AuditModel {

    @Id
    @GeneratedValue(generator = "post_generator")
    @GenericGenerator(name = "post_generator", parameters = {@Parameter(name = "prefix", value = "p"), @Parameter(name = "initialValue", value = "1000")},
            strategy = "clone.reddit.util.Base36IdGenerator")
    private String id;

    @NotBlank
    @Size(max = 256)
    private String title;

    @NotBlank
    private String type;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sub_id")
    private Sub sub;

    @Size(max = 40000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Transient
    private int voteFlag;

    @Transient
    private long grossVotes;

    @Transient
    private long totalComments;
}
