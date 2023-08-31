package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.base.service.BaseService;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.offer.OfferStatus;

import java.util.List;
import java.util.Optional;

public interface OfferService extends BaseService<Offer,Long> {

    @Override
    void save(Offer offer);

    @Override
    void delete(Offer offer);

    @Override
    Optional<Offer> findById(Long aLong);

    @Override
    List<Offer> findAll();

    List<Offer> findOfferListByOrderIdBasedOnProposedPrice(Long orderId);

    List<Offer> findOfferListByOrderIdBasedOnWorkerScore(Long orderId);

//    void editIsAccept(Long offerId, Boolean isAccept);

//    Optional<Offer> findOfferByOrderIdAndIsAccept(Long orderId,boolean isAccept);

    List<Offer> findOffersByWorkerIdAndOfferStatus(Long workerId, OfferStatus offerStatus);

}
