package ir.maktabsharif.homeservicephase2.entity.user;


import ir.maktabsharif.homeservicephase2.entity.Address.Address;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.user.enums.ClientStatus;
import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.MERGE;
import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Client extends Users {

    @Enumerated(value = EnumType.STRING)
    ClientStatus clientStatus;
    @OneToMany(mappedBy = "client", cascade = MERGE, fetch = FetchType.EAGER)
    List<Address> addressList = new ArrayList<>();
    @OneToMany(mappedBy = "client", cascade = MERGE, fetch = FetchType.EAGER)
    List<Order> orderList = new ArrayList<>();

    int paidCounter;

    int numberOfOperation;

    public Client(String firstname, String lastname, String email, String password) {
        super(firstname, lastname, email, password, Role.CLIENT);
        this.clientStatus = ClientStatus.NEW;
        this.paidCounter = 0;
        this.numberOfOperation = 0;
    }

}
