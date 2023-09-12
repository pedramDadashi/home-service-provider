package ir.maktabsharif.homeservicephase2.repository;

import ir.maktabsharif.homeservicephase2.base.repository.BaseRepository;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long> {

    @Override
    boolean existsById(Long aLong);

    @Query("select o from Order o where o.client.email = :clientEmail and o.orderStatus = :orderStatus")
    List<Order> findOrderListByClientEmailAndOrderStatus(String clientEmail, OrderStatus orderStatus);

//    @Modifying
//    @Query(" update Order o set o.orderStatus = :newOrderStatus where o.id = :orderId")
//    void changeOrderStatus(Long orderId, OrderStatus newOrderStatus);

    @Query(" select o from Order o where o.job.name= :jobName and" +
           " (o.orderStatus = :orderStatusOne or o.orderStatus = :orderStatusTwo)")
    List<Order> findByJobNameAndOrderStatus(String jobName, OrderStatus orderStatusOne, OrderStatus orderStatusTwo);

}
