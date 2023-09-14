package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.offer.enums.OfferStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends BaseRepository<Offer, Long> {

    @Query(" select o from Offer o where o.order.id = :orderId and o.offerStatus= :offerStatus order by o.proposedPrice asc")
    List<Offer> findOfferListByOrderIdBasedOnProposedPrice(Long orderId, OfferStatus offerStatus);

    @Query(" select o from Offer o where o.order.id = :orderId and o.offerStatus= :offerStatus order by o.worker.score desc")
    List<Offer> findOfferListByOrderIdBasedOnWorkerScore(Long orderId, OfferStatus offerStatus);

    @Query("select o from Offer o where o.worker.id = :workerId and o.offerStatus = :offerStatus")
    List<Offer> findOffersByWorkerIdAndOfferStatus(Long workerId, OfferStatus offerStatus);

    @Query("select o from Offer o where o.order.id = :orderId and o.offerStatus = :offerStatus")
    Optional<Offer> findByOrderId(Long orderId, OfferStatus offerStatus);


}
