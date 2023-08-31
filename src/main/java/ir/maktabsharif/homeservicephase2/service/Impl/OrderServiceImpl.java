package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.response.OrderResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import ir.maktabsharif.homeservicephase2.exception.JobIsNotExistException;
import ir.maktabsharif.homeservicephase2.mapper.OrderMapper;
import ir.maktabsharif.homeservicephase2.repository.OrderRepository;
import ir.maktabsharif.homeservicephase2.service.JobService;
import ir.maktabsharif.homeservicephase2.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, Long, OrderRepository>
        implements OrderService {

    private final JobService jobService;

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository repository, JobService jobService,
                            OrderMapper orderMapper) {
        super(repository);
        this.jobService = jobService;
        this.orderMapper = orderMapper;
    }


    @Override
    public List<OrderResponseDTO> findAllOrdersByJobName(String jobName) {
        if (jobService.findByName(jobName).isEmpty())
            throw new JobIsNotExistException("this job does not exist!");
        List<Order> orders = repository.findByJobNameAndOrderStatus(jobName,
                OrderStatus.WAITING_FOR_WORKER_SUGGESTION,
                OrderStatus.WAITING_FOR_WORKER_SELECTION);
        List<OrderResponseDTO> orDTOS = new ArrayList<>();
        orders.forEach(o -> orDTOS.add(orderMapper.convertToDTO(o)));
        return orDTOS;
    }
}
