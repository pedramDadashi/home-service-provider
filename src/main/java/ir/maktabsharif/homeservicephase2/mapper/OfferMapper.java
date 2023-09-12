package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.OfferRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.response.OfferResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.util.CustomDuration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferMapper {

    private final CustomDuration customDuration;

    public OfferResponseDTO convertToDTO(Offer offer) {
        return new OfferResponseDTO(
                offer.getId(),
                offer.getWorker().getId(),
                offer.getWorker().getUsername(),
                offer.getOrder().getId(),
                offer.getProposedPrice(),
                offer.getOfferStatus(),
                offer.getExecutionTime(),
                offer.getEndTime(),
                customDuration.getDuration(offer.getExecutionTime(),
                        offer.getEndTime(), offer.getClass().getName())
        );
    }

    public Offer convertToNewOffer(OfferRequestDTO dto) {
        return new Offer(
                dto.getProposedStartDate(),
                dto.getProposedEndDate(),
                dto.getOfferProposedPrice()
        );
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
