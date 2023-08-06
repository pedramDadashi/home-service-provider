package ir.maktabsharif.homeservicephase2.entity.job;


import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Job extends BaseEntity<Long>
        implements Serializable {

    private String name;
    private Long basePrice;
    private String description;
    @ManyToOne
    private MainService mainService;
    @ManyToMany
    private Set<Worker> workerSet = new HashSet<>();
    @OneToMany(mappedBy = "job")
    private List<Order> orderList = new ArrayList<>();

    public Job(String name, Long basePrice, String description, MainService mainService) {
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
        this.mainService = mainService;
    }

    @Override
    public String toString() {
        return "SubServiceService{" +
               "name='" + name + '\'' +
               ", basePrice=" + basePrice +
               ", description='" + description + '\'' +
//               ", main-service=" + mainService.getName() +
               '}';
    }

}
