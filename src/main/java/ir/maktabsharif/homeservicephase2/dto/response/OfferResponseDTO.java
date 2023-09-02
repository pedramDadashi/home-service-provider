package ir.maktabsharif.homeservicephase2.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponseDTO {

    private Long offerId;
    private Long workerId;
    private Long orderId;
    private Long offerPrice;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime proposedStartDate;
//    private int durationOfWork;

}
