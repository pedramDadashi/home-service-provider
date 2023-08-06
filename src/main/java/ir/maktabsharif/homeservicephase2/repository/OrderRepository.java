package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long> {

    List<Order> findOrderByClientEmail(String clientEmail);

    List<Order> findOrderByClientEmailAndOrderStatus(String clientEmail, OrderStatus orderStatus);

    @Query(" update Order o set o.orderStatus = :newOrderStatus where o.id = :orderId and o.orderStatus = :orderStatus")
    void changeOrderStatus(Long orderId, OrderStatus orderStatus, OrderStatus newOrderStatus);

    @Query(" from Order o where o.job.id = :jobId and (o.orderStatus = :orderStatusOne or o.orderStatus = :orderStatusTwo)")
    List<Order> findByJobIdAndOrderStatus(Long jobId, OrderStatus orderStatusOne, OrderStatus orderStatusTwo);

}
