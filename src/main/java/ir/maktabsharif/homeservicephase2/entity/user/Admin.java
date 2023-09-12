package ir.maktabsharif.homeservicephase2.entity.user;


import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static ir.maktabsharif.homeservicephase2.entity.user.enums.Role.ADMIN;
import static lombok.AccessLevel.PRIVATE;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Admin extends Users {

    boolean isManager;

    public Admin(String firstname, String lastname, String email, String password, Role role) {
        super(firstname, lastname, email, password, role);
        if (role.name().equals(ADMIN.name()))
            this.isManager = false;
        else
            this.isManager = true;
    }
}
