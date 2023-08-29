package ir.maktabsharif.homeservicephase2.dto.response;


import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferResponseDTO {

    private Worker worker;
    private Order order;
    private Long offerPrice;
    private Date proposedStartDate;
    private int durationOfWork;

}
