package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.offer.OfferStatus;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends BaseRepository<Offer, Long> {

    @Query(" select o from Offer o where o.order.id = :orderId order by o.proposedPrice")
    List<Offer> findOfferListByOrderIdBasedOnProposedPrice(Long orderId);

    @Query(" select o from Offer o where o.order.id = :orderId order by o.worker.score desc")
    List<Offer> findOfferListByOrderIdBasedOnWorkerScore(Long orderId);

    @Query("select o from Offer o where o.worker.id = :workerId and o.offerStatus = :offerStatus")
    List<Offer> findOffersByWorkerIdAndOfferStatus(Long workerId, OfferStatus offerStatus);

//    @Query(" update Offer o set o.isAccept = :isAccept where o.id = :offerId")
//    void editIsAccept(Long offerId, Boolean isAccept);

//    @Query("select o from Offer o where o.worker.id = :workerId and o.isAccept = :isAccept")
//    List<Offer> findOfferListByWorkerIdAndIsAccept(Long workerId, boolean isAccept);
//
//    @Query("select o from Offer o where o.order.id = :orderId and o.isAccept = :isAccept")
//    Optional<Offer> findOfferByOrderIdAndIsAccept(Long orderId,boolean isAccept);

    List<Offer> findAllByOrder(Order order);

    List<Offer> findAllByWorker(Worker worker);

    @Query("select o from Offer o where o.order.id = :orderId and o.offerStatus = :offerStatus")
    Optional<Offer> findByOrderId(Long orderId, OfferStatus offerStatus);

//    List<Offer> findOffersByExpertAndOfferStatus(Expert expert, OfferStatus offerStatus);

}
