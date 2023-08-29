package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long> {

    List<Order> findOrderByClientEmail(String clientEmail);

    @Query("select o from Order o where o.client.email = :clientEmail and o.orderStatus = :orderStatus")
    List<Order> findOrderListByClientEmailAndOrderStatus(String clientEmail, OrderStatus orderStatus);

    @Modifying
    @Query(" update Order o set o.orderStatus = :newOrderStatus where o.id = :orderId")
    void changeOrderStatus(Long orderId, OrderStatus newOrderStatus);

    @Query(" select o from Order o where o.job.id = :jobId and" +
           " (o.orderStatus = :orderStatusOne or o.orderStatus = :orderStatusTwo)")
    List<Order> findByJobIdAndOrderStatus(Long jobId, OrderStatus orderStatusOne, OrderStatus orderStatusTwo);

}
