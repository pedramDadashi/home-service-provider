package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.base.service.BaseService;
import ir.maktabsharif.homeservicephase2.dto.request.FilterOrderDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterOrderResponseDTO;
import ir.maktabsharif.homeservicephase2.dto.response.LimitedOrderResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.order.Order;

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

//    List<Order> findByClientEmailAndOrderStatus(String clientEmail, OrderStatus orderStatus);

//    void changeOrderStatus(Long orderId, OrderStatus newOrderStatus);

//    List<Order> findByJobIdAndOrderStatus(Long jobId, OrderStatus orderStatusOne, OrderStatus orderStatusTwo);

    List<FilterOrderResponseDTO> findAllOrdersByJobName(String jobName);

    List<FilterOrderResponseDTO> ordersFilter(FilterOrderDTO filterOrderDTO);

//    int numberOfSubmitOrders(Long customerId);
//
//    int numberOfSubmitOrdersByOrderStatus(Long customerId, OrderStatus orderStatus);

    void chooseOffer(Order order,Long offerId);

    List<LimitedOrderResponseDTO> findAllOrdersByJobNameAndProvince(String jobName, String workerProvince);
}
