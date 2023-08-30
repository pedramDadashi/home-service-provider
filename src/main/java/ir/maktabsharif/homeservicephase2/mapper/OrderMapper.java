package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.OrderRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.response.OrderResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OfferMapper offerMapper;

    public OrderResponseDTO convertToDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setClientId(order.getClient().getId());
        orderResponseDTO.setJobId(order.getJob().getId());
        orderResponseDTO.setOrderStatus(order.getOrderStatus());
        orderResponseDTO.setAddress(order.getAddress());
        orderResponseDTO.setComment(order.getComment());
        orderResponseDTO.setDescription(order.getDescription());
        orderResponseDTO.setOffers(order.getOfferList());
        orderResponseDTO.setClientProposedPrice(order.getProposedPrice());
        orderResponseDTO.setOrderRegistrationDate(order.getExecutionTime());
        orderResponseDTO.setClientProposedPrice(order.getProposedPrice());
        return orderResponseDTO;
    }

    public Order convertToClient(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setClient(orderRequestDTO.getClient());
        order.setOrderStatus(orderRequestDTO.getOrderStatus());
        List<Offer> offerList = new ArrayList<>();
        orderRequestDTO.getOfferRequestDTOList().forEach(orDTOs -> offerList.add(offerMapper.convertToOffer(orDTOs)));
        order.setOfferList(offerList);
        return order;
    }

}
