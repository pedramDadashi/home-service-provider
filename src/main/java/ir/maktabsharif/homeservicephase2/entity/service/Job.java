package ir.maktabsharif.homeservicephase2.entity.service;


import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Job extends BaseEntity<Long> {

    String name;
    Long basePrice;
    String description;
    @ManyToOne
    MainService mainService;
    @ManyToMany(cascade = ALL, fetch = EAGER)
    List<Worker> workerList = new ArrayList<>();
    @OneToMany(mappedBy = "job")
    List<Order> orderList = new ArrayList<>();

    public Job(String name, Long basePrice, String description, MainService mainService) {
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
        this.mainService = mainService;
    }

}
