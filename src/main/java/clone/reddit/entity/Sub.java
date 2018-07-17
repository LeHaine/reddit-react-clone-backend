package clone.reddit.entity;

import clone.reddit.entity.type.AccountContainingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by colt on 7/9/18.
 */
@Entity
@Table(name = "sub")
@Getter
@Setter
public class Sub extends AccountValidatorModel{

    @Id
    @GeneratedValue(generator = "sub_generator")
    @SequenceGenerator(name = "sub_generator", sequenceName = "sub_seq", initialValue = 1000)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    @NotBlank
    @Size(max = 30)
    private String name;
}
