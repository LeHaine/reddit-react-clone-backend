package clone.reddit.entity;

import clone.reddit.entity.type.AccountContainingEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by colt on 7/8/18.
 */
@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment extends AccountValidatorModel {


    @Id
    @GeneratedValue(generator = "comment_generator")
    @GenericGenerator(name = "comment_generator", parameters = {@Parameter(name = "prefix", value = "c"), @Parameter(name = "initialValue", value = "1000")},
            strategy = "clone.reddit.util.Base36IdGenerator")
    private String id;

    @NotBlank
    private String text;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Formula("(select count(*) from Vote v where v.comment_id = id and v.flag = 1) - (select count(*) from Vote v where v.comment_id = id and v.flag = -1)")
    private long grossVotes;

    @Formula("COALESCE((select v.flag from Vote v join Account a on v.account_id = a.id where a.username = '{USERNAME}' and v.comment_id = id), 0)")
    private long voteFlag;
}
