package ir.maktabsharif.homeservicephase2.entity.user;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Admin extends Users {

    private boolean isAdmin;

    public Admin(String firstname, String lastname
            , String email, String password, Boolean isActive, boolean isAdmin) {
        super(firstname, lastname, email, password, isActive);
        this.isAdmin = isAdmin;
    }
}
