package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.response.OfferResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferMapper {

//    private final OrderService orderService;
//    private final WorkerService workerService;

    public OfferResponseDTO convertToDTO(Offer offer) {
        OfferResponseDTO offerResponseDTO = new OfferResponseDTO();
        offerResponseDTO.setOfferId(offer.getId());
        offerResponseDTO.setOrderId(offer.getOrder().getId());
        offerResponseDTO.setWorkerId(offer.getWorker().getId());
        offerResponseDTO.setProposedStartDate(offer.getExecutionTime());
        offerResponseDTO.setOfferPrice(offer.getProposedPrice());
        return offerResponseDTO;
    }

//    public Offer convertToOffer(OfferRequestDTO offerRequestDTO) {
//        Offer offer = new Offer();
//        offer.setOrder(orderService.findById(offerRequestDTO.getOrderId()).get());
//        offer.setId(offerRequestDTO.getOfferId());
//        offer.setWorker(workerService.findById(offerRequestDTO.getWorkerId()).get());
//        offer.setProposedPrice(offerRequestDTO.getOfferPrice());
//        offer.setExecutionTime(offerRequestDTO.getProposedStartDate());
//        return offer;
//    }

}
