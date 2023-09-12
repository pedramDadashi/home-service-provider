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

    public Order convertToOrderWithSubmitDTO(SubmitOrderDTO soDTO, Client client, Job job) {
        return new Order(
                soDTO.getWorkStartDate(),
                soDTO.getWorkEndDate(),
                soDTO.getClientProposedPrice(),
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
                order.getAddress(),
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
                (order.getAddress().getProvince() + " , "
                 + order.getAddress().getCity() + " , "
                 + order.getAddress().getAvenue()
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
//
//    private FilterOrderResponseDTO GetDuration(Order order, FilterOrderResponseDTO dto) {
//        OrderStatus orderStatus = order.getOrderStatus();
//        StringBuilder durationOfWork = new StringBuilder();
//        if (orderStatus.equals(WAITING_FOR_WORKER_SUGGESTION) || orderStatus.equals(WAITING_FOR_WORKER_SELECTION)) {
//            java.time.Duration durationOrder = java.time.Duration.between(order.getExecutionTime(), order.getEndTime());
//            durationOfWork
//                    .append("No offer has been registered or confirmed for this order yet, but the expected duration is ")
//                    .append(TimeCalculationAndConvertToString(durationOrder));
//            dto.setDurationOfWork(durationOfWork.toString());
//            return dto;
//        } else if (orderStatus.equals(WAITING_FOR_WORKER_TO_COME)) {
//            durationOfWork.append("This Order is not started yet, but the duration recorded is ");
//        } else if (orderStatus.equals(STARTED)) {
//            durationOfWork.append("This Order is not finished yet, but the duration recorded is ");
//        } else {
//            durationOfWork.append("The duration of this order is ");
//        }
//        return GetOfferDuration(order, dto, durationOfWork);
//    }
//
//    private FilterOrderResponseDTO GetOfferDuration(Order order, FilterOrderResponseDTO dto,
//                                                    StringBuilder durationOfWork) {
//        final LocalDateTime[] startTimeAndEndTime = new LocalDateTime[2];
//        final Long[] offerProposedPrice = new Long[1];
//        order.getOfferList().forEach(o -> {
//            if (o.getOfferStatus().equals(ACCEPTED)) {
//                startTimeAndEndTime[0] = o.getExecutionTime();
//                startTimeAndEndTime[1] = o.getEndTime();
//                offerProposedPrice[0] = o.getProposedPrice();
//            }
//        });
//        java.time.Duration durationOffer = java.time.Duration.between(startTimeAndEndTime[0], startTimeAndEndTime[1]);
//        durationOfWork.append(TimeCalculationAndConvertToString(durationOffer));
//        dto.setOfferProposedPrice(offerProposedPrice[0]);
//        dto.setOfferCreationDate(startTimeAndEndTime[0]);
//        dto.setDurationOfWork(durationOfWork.toString());
//        return dto;
//    }
//
//    private String TimeCalculationAndConvertToString(java.time.Duration durationOffer) {
//        StringBuilder calculationDurationToString = new StringBuilder();
//        if (durationOffer.toDaysPart() > 0) {
//            if (durationOffer.toDaysPart() > 1)
//                calculationDurationToString.append(" Days: ");
//            else calculationDurationToString.append(" Day: ");
//            calculationDurationToString.append(durationOffer.toDaysPart());
//        }
//        if (durationOffer.toHoursPart() > 0) {
//            if (durationOffer.toHoursPart() > 1)
//                calculationDurationToString.append(" ,Hours: ");
//            else calculationDurationToString.append(" ,Hour: ");
//            calculationDurationToString.append(durationOffer.toHoursPart());
//        }
//        if (durationOffer.toMinutesPart() > 0) {
//            if (durationOffer.toMinutesPart() > 1)
//                calculationDurationToString.append(" ,Minutes: ");
//            else calculationDurationToString.append(" ,Minute: ");
//            calculationDurationToString.append(durationOffer.toMinutesPart());
//        }
//        return calculationDurationToString.toString();
//    }
//

}
