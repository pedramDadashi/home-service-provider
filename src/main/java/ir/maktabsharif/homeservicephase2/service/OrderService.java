package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.base.service.BaseService;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService extends BaseService<Order,Long> {

    @Override
    void save(Order order);

    @Override
    void delete(Order order);

    @Override
    Optional<Order> findById(Long aLong);

    @Override
    List<Order> findAll();

    List<Order> findByClientIdAndOrderStatus(Long clientId, OrderStatus orderStatus);

    void changeOrderStatus(Long orderId, OrderStatus orderStatus, OrderStatus newOrderStatus);

    List<Order> findByJobIdAndOrderStatus(Long jobId, OrderStatus orderStatusOne, OrderStatus orderStatusTwo);

}
