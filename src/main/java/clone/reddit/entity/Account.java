package clone.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    private long id;


    @Column(unique = true)
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @Column(length = 2048)
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
