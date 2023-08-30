package ir.maktabsharif.homeservicephase2.dto.request;

import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

//    private String orderNumber;
    private Client client;
    private List<OfferRequestDTO> offerRequestDTOList = new ArrayList<>();
    private OrderStatus orderStatus;

}
