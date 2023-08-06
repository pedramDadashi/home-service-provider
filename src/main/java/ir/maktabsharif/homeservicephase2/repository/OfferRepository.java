package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends BaseRepository<Offer, Long> {

    @Query(" select o from Offer o where o.order.id = :orderId order by o.proposedPrice")
    List<Offer> findByOrderIdBasedOnProposedPrice(Long orderId);

    @Query(" select o from Offer o where o.order.id = :orderId order by o.worker.score desc")
    List<Offer> findByOrderIdBasedOnWorkerScore(Long orderId);

    @Query(" update Offer o set o.isAccept = :isAccept where o.id = :offerId")
    void editIsAccept(Long offerId, Boolean isAccept);

    @Query("select o from Offer o where o.worker.id = :workerId and o.isAccept = :isAccept")
    List<Offer> findOffersByWorkerIdAndIsAccept(Long workerId, boolean isAccept);

}
