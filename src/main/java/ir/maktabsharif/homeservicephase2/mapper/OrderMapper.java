package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.SubmitOrderDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterOrderResponseDTO;
import ir.maktabsharif.homeservicephase2.dto.response.LimitedOrderResponseDTO;
import ir.maktabsharif.homeservicephase2.dto.response.OrderResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.service.Job;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import ir.maktabsharif.homeservicephase2.util.CustomDuration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static ir.maktabsharif.homeservicephase2.entity.offer.enums.OfferStatus.ACCEPTED;
import static ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus.WAITING_FOR_WORKER_SELECTION;
import static ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus.WAITING_FOR_WORKER_SUGGESTION;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final CustomDuration customDuration;

    public Order convertToNewOrder(SubmitOrderDTO soDTO, Client client, Job job) {
        return new Order(
                soDTO.getWorkStartDate(),
                soDTO.getWorkEndDate(),
                soDTO.getProposedPrice(),
                soDTO.getDescription(),
                client.getAddressList().stream().filter(a ->
                        a.getTitle().equals(soDTO.getAddressTitle())).findFirst().get(),
                client,
                job
        );
    }

    public OrderResponseDTO convertToDTO(Order order) {
        return new OrderResponseDTO(
                order.getAddress(),
                order.getDescription(),
                order.getProposedPrice(),
                order.getOrderStatus(),
                order.getOfferList(),
                order.getJob().getName(),
                order.getJob().getMainService().getName(),
                order.getExecutionTime(),
                order.getEndTime(),
                customDuration.getDuration(order.getExecutionTime(),
                        order.getEndTime(), order.getClass().getName())
        );
    }

    public FilterOrderResponseDTO convertToFilterDTO(Order order) {
        FilterOrderResponseDTO fdto = new FilterOrderResponseDTO(
                ("Title: " + order.getAddress().getTitle() +
                 " ,Address: " + order.getAddress().getProvince() +
                 " ," + order.getAddress().getCity() +
                 " ," + order.getAddress().getAvenue() +
                 " ," + order.getAddress().getMoreDescription() +
                 " ,HouseNumber: " + order.getAddress().getHouseNumber() +
                 " ,PostalCode: " + order.getAddress().getPostalCode()),
                order.getDescription(),
                order.getProposedPrice(),
                order.getOrderStatus(),
                order.getJob().getName(),
                order.getJob().getMainService().getName(),
                order.getExecutionTime(),
                customDuration.getDuration(
                        order.getExecutionTime(),
                        order.getEndTime(),
                        order.getClass().getName())

        );
        if (order.getOrderStatus().equals(WAITING_FOR_WORKER_SUGGESTION) ||
            order.getOrderStatus().equals(WAITING_FOR_WORKER_SELECTION))
            fdto.setAcceptedOfferId(-1L);
        else
            fdto.setAcceptedOfferId(
                    order.getOfferList().stream().filter(o ->
                            o.getOfferStatus().equals(ACCEPTED)).findFirst().get().getId()
            );
        return fdto;
    }

    public LimitedOrderResponseDTO convertToLimitedDto(Order order) {
        return new LimitedOrderResponseDTO(
                (order.getAddress().getProvince() + " ," +
                 order.getAddress().getCity() + " ," +
                 order.getAddress().getAvenue() + " , ..."
                ),
                order.getDescription(),
                order.getProposedPrice(),
                order.getOrderStatus(),
                order.getJob().getName(),
                order.getJob().getMainService().getName(),
                order.getExecutionTime(),
                order.getEndTime(),
                customDuration.getDuration(order.getExecutionTime(),
                        order.getEndTime(), order.getClass().getName())
        );
    }
}
