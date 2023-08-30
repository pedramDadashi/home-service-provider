package ir.maktabsharif.homeservicephase2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitOrderDTO {

    private Long clientId;
    private Long jobId;
    private Long ClientProposedPrice;
    private String description;
    private LocalDateTime workStartDate;
    private String address;
    private LocalDateTime workEndDate;

}
