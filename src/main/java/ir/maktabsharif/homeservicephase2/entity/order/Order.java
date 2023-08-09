package ir.maktabsharif.homeservicephase2.entity.order;

import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "OrderTable")
public class Order extends BaseEntity<Long> {

    private Long proposedPrice;
    private String description;
    private LocalDateTime executionTime;
    private String address;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime updateTime;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Client client;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Job job;
    @OneToMany(mappedBy = "order")
    private List<Offer> offerList=new ArrayList<>();

    public Order(Long proposedPrice, String description, LocalDateTime executionTime
            , String address, LocalDateTime updateTime, Client client, Job job) {
        this.proposedPrice = proposedPrice;
        this.description = description;
        this.executionTime = executionTime;
        this.address = address;
        this.updateTime = updateTime;
        this.client = client;
        this.job = job;
        this.orderStatus=OrderStatus.WAITING_FOR_WORKER_SUGGESTION;
    }
}
