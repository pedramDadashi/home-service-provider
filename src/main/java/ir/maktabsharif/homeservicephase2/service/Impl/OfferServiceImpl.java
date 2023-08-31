package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.offer.OfferStatus;
import ir.maktabsharif.homeservicephase2.repository.OfferRepository;
import ir.maktabsharif.homeservicephase2.service.OfferService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl extends BaseServiceImpl<Offer, Long, OfferRepository>
        implements OfferService {


    public OfferServiceImpl(OfferRepository repository) {
        super(repository);
    }

    @Override
    public List<Offer> findOfferListByOrderIdBasedOnProposedPrice(Long orderId) {
        return repository.findOfferListByOrderIdBasedOnProposedPrice(orderId);
    }

    @Override
    public List<Offer> findOfferListByOrderIdBasedOnWorkerScore(Long orderId) {
        return repository.findOfferListByOrderIdBasedOnWorkerScore(orderId);
    }

    @Override
    public List<Offer> findOffersByWorkerIdAndOfferStatus(Long workerId, OfferStatus offerStatus) {
        return repository.findOffersByWorkerIdAndOfferStatus(workerId, offerStatus);
    }
}
