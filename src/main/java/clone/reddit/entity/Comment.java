package clone.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by colt on 7/8/18.
 */
@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment extends AuditModel {


    @Id
    @GeneratedValue(generator = "comment_generator")
    @GenericGenerator(name = "comment_generator", parameters = {@Parameter(name = "prefix", value = "c"), @Parameter(name = "initialValue", value = "1000")},
            strategy = "clone.reddit.util.Base36IdGenerator")
    private String id;

    @NotBlank
    private String text;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @OneToMany(mappedBy = "comment")
    private List<Vote> votes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
