package ir.maktabsharif.homeservicephase2.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import ir.maktabsharif.homeservicephase2.entity.offer.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class OfferResponseDTO {

    Long offerId;
    Long workerId;
    String workerUsername;
    Long orderId;
    Long offerProposedPrice;
    OfferStatus offerStatus;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    LocalDateTime proposedStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    LocalDateTime proposedEndDate;
    String durationOfWork;
}
