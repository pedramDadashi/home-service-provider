package ir.maktabsharif.homeservicephase2.entity.user;

import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter

public class Users extends BaseEntity<Long> {

    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String email;
    private String password;
    private Boolean isActive;
    private Long credit;

    public Users(String firstname, String lastname
            , String email, String password
            , Boolean isActive/* ,Role role*/) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.credit = 0L;
    }

    @Override
    public String toString() {
        return "Person{" +
               "firstname='" + firstname + '\'' +
               ", lastname='" + lastname + '\'' +
               ", email='" + email + '\'' +
               ", username='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
