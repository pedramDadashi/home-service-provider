package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.base.service.BaseService;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;

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

    List<Offer> findByOrderIdBasedOnProposedPrice(Long orderId);

    List<Offer> findByOrderIdBasedOnExpertScore(Long orderId);

    void editIsAccept(Long offerId, Boolean isAccept);

}
