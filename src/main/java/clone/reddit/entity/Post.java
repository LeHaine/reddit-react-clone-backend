package clone.reddit.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by colt on 7/8/18.
 */
@Entity
@Table(name = "thread")
public class Thread extends AuditModel {

    @Id
    @GeneratedValue(generator = "thread_generator")
    @SequenceGenerator(name = "thread_generator", sequenceName = "thread_seq", initialValue = 1000)
    private Long id;

    @NotBlank
    @Size(max = 256)
    private String title;

    @NotBlank
    private String type;

    @NotBlank
    private String sub;

    private String content;

    @OneToMany(mappedBy = "thread")
    private List<Vote> votes;

    @OneToMany(mappedBy = "thread")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Thread() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Thread{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", sub='" + sub + '\'' +
                ", content='" + content + '\'' +
                ", account=" + account.getUsername() +
                '}';
    }
}
