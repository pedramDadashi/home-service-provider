package ir.maktabsharif.homeservicephase2.dto.request;

import ir.maktabsharif.homeservicephase2.entity.offer.TimeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferRequestDTO {

    private Long orderId;
    //    private Long offerId;
    private Long workerId;
    private Long offerPrice;
    private LocalDateTime proposedStartDate;
    private TimeType type;
    private int durationOfWork;
    private LocalDateTime proposedEndDate;

}
