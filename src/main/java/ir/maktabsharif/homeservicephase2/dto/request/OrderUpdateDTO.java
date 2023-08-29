package ir.maktabsharif.homeservicephase2.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDTO {

    private Long orderId;
    private String description;
    private Double ClientProposedPrice;
    private LocalDateTime workStartDate;
    private int durationOfWork;
    private String address;

}
