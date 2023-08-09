package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import ir.maktabsharif.homeservicephase2.repository.OrderRepository;
import ir.maktabsharif.homeservicephase2.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, Long, OrderRepository>
        implements OrderService {


    public OrderServiceImpl(OrderRepository repository) {
        super(repository);
    }

    @Override
    public List<Order> findByClientEmailAndOrderStatus(String clientEmail, OrderStatus orderStatus) {
        return repository.findOrderListByClientEmailAndOrderStatus(clientEmail, orderStatus);
    }

    @Override
    public void changeOrderStatus(Long orderId, OrderStatus orderStatus, OrderStatus newOrderStatus) {
        repository.changeOrderStatus(orderId, orderStatus, newOrderStatus);
    }

    @Override
    public List<Order> findByJobIdAndOrderStatus(Long jobId, OrderStatus orderStatusOne, OrderStatus orderStatusTwo) {
        return repository.findByJobIdAndOrderStatus(jobId, orderStatusOne, orderStatusTwo);
    }
}
