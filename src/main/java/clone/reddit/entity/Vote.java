package clone.reddit.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by colt on 7/8/18.
 */
@Entity
@Table(name = "vote")
@Getter
@Setter
public class Vote extends AuditModel {

    @Id
    @GeneratedValue(generator = "vote_generator")
    @SequenceGenerator(name = "vote_generator", sequenceName = "vote_seq", initialValue = 1000)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private int flag;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}
