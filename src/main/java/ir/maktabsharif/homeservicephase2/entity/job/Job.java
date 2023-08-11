package ir.maktabsharif.homeservicephase2.entity.job;


import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;


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
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job that = (Job) o;
        return Objects.equals(description, that.description);
    }
    @Override
    public int hashCode() {
        return Objects.hash(description);
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
