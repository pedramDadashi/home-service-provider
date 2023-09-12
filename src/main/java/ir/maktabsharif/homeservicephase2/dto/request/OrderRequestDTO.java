package ir.maktabsharif.homeservicephase2.dto.request;

import ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequestDTO {

     List<OfferRequestDTO> offerRequestDTOList = new ArrayList<>();
     OrderStatus orderStatus;

}
