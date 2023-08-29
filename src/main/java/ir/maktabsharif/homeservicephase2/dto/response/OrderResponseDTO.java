package ir.maktabsharif.homeservicephase2.dto.response;

import ir.maktabsharif.homeservicephase2.entity.comment.Comment;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private String orderNumber;
    private Long clientId;
    private Long jobId;
    private List<Offer> offers = new ArrayList<>();
    private String description;
    private Double ClientProposedPrice;
    private OrderStatus orderStatus;
    private Comment comment;
    private Date orderRegistrationDate;
    private Date workStartDate;
    private int durationOfWork;
    private String address;

}
