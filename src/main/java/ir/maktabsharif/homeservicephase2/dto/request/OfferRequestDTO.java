package ir.maktabsharif.homeservicephase2.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.maktabsharif.homeservicephase2.entity.offer.TimeType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferRequestDTO {

    private Long orderId;
    private Long offerId;
    private Long workerId;
    private Long offerPrice;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime proposedStartDate;
    @Enumerated
    private TimeType type;
    private int durationOfWork;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime proposedEndDate;

}
