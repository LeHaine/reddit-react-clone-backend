package clone.reddit.entity;

import clone.reddit.entity.listeners.AccountContainingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Created by colt on 7/17/18.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AccountContainingEntityListener.class)
@JsonIgnoreProperties(value = {"account"}, allowGetters = true)
public class AccountValidatorModel extends AuditModel {


    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
