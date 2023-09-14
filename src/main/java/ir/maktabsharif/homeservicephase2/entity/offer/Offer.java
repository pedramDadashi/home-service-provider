package ir.maktabsharif.homeservicephase2.entity.offer;


import com.fasterxml.jackson.annotation.JsonFormat;
import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.offer.enums.OfferStatus;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Offer extends BaseEntity<Long> {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime executionTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endTime;
    Long proposedPrice;
    @Enumerated(value = EnumType.STRING)
    OfferStatus offerStatus;
    @ManyToOne
    Order order;
    @ManyToOne
    Worker worker;

    public Offer(LocalDateTime executionTime, LocalDateTime endTime, Long proposedPrice) {
        this.executionTime = executionTime;
        this.endTime = endTime;
        this.proposedPrice = proposedPrice;
        this.offerStatus = OfferStatus.WAITING;
    }

}
