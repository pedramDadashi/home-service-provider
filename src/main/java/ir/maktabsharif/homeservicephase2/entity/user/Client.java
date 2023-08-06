package ir.maktabsharif.homeservicephase2.entity.user;


import ir.maktabsharif.homeservicephase2.entity.order.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor

public class Client extends Users {

    @OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
    private List<Order> orderList = new ArrayList<>();

    public Client(String firstname, String lastname, String email
            , String password) {
        super(firstname, lastname, email, password, Boolean.TRUE);
    }


    @Override
    public String toString() {
        return "Client {" +
               "firstname='" + getFirstname() + '\'' +
               ", lastname='" + getLastname() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", username='" + getEmail() + '\'' +
               "credit=" + getCredit() +
               "} ";
    }

}
