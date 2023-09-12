package ir.maktabsharif.homeservicephase2.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferRequestDTO {

    Long orderId;
    Long offerProposedPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime proposedStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime proposedEndDate;

}
