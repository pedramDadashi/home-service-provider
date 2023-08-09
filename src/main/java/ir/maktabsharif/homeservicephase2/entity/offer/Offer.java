package ir.maktabsharif.homeservicephase2.entity.offer;


import ir.maktabsharif.homeservicephase2.base.entity.BaseEntity;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Offer extends BaseEntity<Long> {

    private String offerName;
    private Long proposedPrice;
    private LocalDateTime executionTime;
    @Enumerated(value = EnumType.STRING)
    private TimeType timeType;
    private Integer durationTime;
    private Boolean isAccept;
    private LocalDateTime endTime;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Worker worker;

    public Offer(String offer, Long proposedPrice, LocalDateTime executionTime
            , TimeType timeType, Integer durationTime
            , LocalDateTime endTime) {
        this.offerName = offer;
        this.proposedPrice = proposedPrice;
        this.executionTime = executionTime;
        this.timeType = timeType;
        this.durationTime = durationTime;
        this.endTime = endTime;
        this.isAccept = false;
    }

    @Override
    public String toString() {
        return "Offer {" +
               "offer='" + offerName + '\'' +
               ", proposedPrice=" + proposedPrice +
               ", executionTime=" + executionTime +
               ", timeType=" + timeType +
               ", durationTime=" + durationTime +
               ", isAccept=" + isAccept +
               "} ";
    }
}
