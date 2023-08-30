package ir.maktabsharif.homeservicephase2.mapper;

import ir.maktabsharif.homeservicephase2.dto.request.OfferRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.response.OfferResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.service.OrderService;
import ir.maktabsharif.homeservicephase2.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfferMapper {

    private final OrderService orderService;
    private final WorkerService workerService;

    public OfferResponseDTO convertToDTO(Offer offer) {
        OfferResponseDTO offerResponseDTO = new OfferResponseDTO();
        offerResponseDTO.setOrder(offer.getOrder());
        offerResponseDTO.setWorker(offer.getWorker());
        offerResponseDTO.setProposedStartDate(offer.getExecutionTime());
        offerResponseDTO.setOfferPrice(offer.getProposedPrice());
        return offerResponseDTO;
    }

    public Offer convertToOffer(OfferRequestDTO offerRequestDTO) {
        Offer offer = new Offer();
        offer.setOrder(orderService.findById(offerRequestDTO.getOrderId()).get());
        offer.setId(offerRequestDTO.getOfferId());
        offer.setWorker(workerService.findById(offerRequestDTO.getWorkerId()).get());
        offer.setProposedPrice(offerRequestDTO.getOfferPrice());
        offer.setExecutionTime(offerRequestDTO.getProposedStartDate());
        return offer;
    }

}