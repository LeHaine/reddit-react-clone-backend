package clone.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by colt on 7/8/18.
 */
@Entity
@Table(name = "account")
@Getter @Setter
public class Account extends AuditModel {

    @Id
    @GeneratedValue(generator = "account_generator")
    @SequenceGenerator(name = "account_generator", sequenceName = "account_seq", initialValue = 1000)
    private long id;


    @Column(unique = true)
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @Column(length = 2048)
    @NotBlank
    @JsonIgnore
    private String password;

}
