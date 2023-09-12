package ir.maktabsharif.homeservicephase2.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.Address.Address;
import ir.maktabsharif.homeservicephase2.entity.comment.Comment;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.service.Job;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus.WAITING_FOR_WORKER_SUGGESTION;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PRIVATE;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Order_Table")
@FieldDefaults(level = PRIVATE)
public class Order extends BaseEntity<Long> {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime executionTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endTime;
    Long proposedPrice;
    String description;
    @Enumerated(value = STRING)
    OrderStatus orderStatus;
    @ManyToOne(cascade = MERGE)
    Client client;
    @ManyToOne(cascade = MERGE)
    Job job;
    @OneToMany(mappedBy = "order")
    List<Offer> offerList = new ArrayList<>();
    @OneToOne
    Comment comment;
    @OneToOne
    Address address;

    public Order(LocalDateTime executionTime, LocalDateTime endTime, Long proposedPrice,
                 String description, Address address, Client client, Job job) {
        this.executionTime = executionTime;
        this.endTime = endTime;
        this.proposedPrice = proposedPrice;
        this.description = description;
        this.address = address;
        this.orderStatus = WAITING_FOR_WORKER_SUGGESTION;
        this.client = client;
        this.job = job;
    }
}
