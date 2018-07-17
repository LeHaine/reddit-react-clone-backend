package clone.reddit.entity;

import clone.reddit.entity.type.AccountContainingEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
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
public class Post extends AccountValidatorModel {

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

    @Formula("(select count(*) from Vote v where v.post_id = id and v.flag = 1) - (select count(*) from Vote v where v.post_id = id and v.flag = -1)")
    private long grossVotes;

    @Formula("COALESCE((select v.flag from Vote v join Account a on v.account_id = a.id where a.username = '{USERNAME}' and v.post_id = id), 0)")
    private long voteFlag;

    @Formula("(select count(*) from Comment c where c.post_id = id)")
    private long totalComments;
}
