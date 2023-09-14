package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.request.FilterOrderDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterOrderResponseDTO;
import ir.maktabsharif.homeservicephase2.dto.response.LimitedOrderResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.exception.JobIsNotExistException;
import ir.maktabsharif.homeservicephase2.mapper.OrderMapper;
import ir.maktabsharif.homeservicephase2.repository.OrderRepository;
import ir.maktabsharif.homeservicephase2.service.JobService;
import ir.maktabsharif.homeservicephase2.service.MainServiceService;
import ir.maktabsharif.homeservicephase2.service.OrderService;
import ir.maktabsharif.homeservicephase2.util.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ir.maktabsharif.homeservicephase2.entity.offer.enums.OfferStatus.ACCEPTED;
import static ir.maktabsharif.homeservicephase2.entity.offer.enums.OfferStatus.REJECTED;
import static ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus.*;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, Long, OrderRepository>
        implements OrderService {

    private final JobService jobService;
    private final MainServiceService mainServiceService;

    private final OrderMapper orderMapper;

    private final Validation validation;
    private final EntityManager entityManager;

    public OrderServiceImpl(OrderRepository repository, JobService jobService,
                            MainServiceService mainServiceService, OrderMapper orderMapper,
                            Validation validation, EntityManager entityManager) {
        super(repository);
        this.jobService = jobService;
        this.mainServiceService = mainServiceService;
        this.orderMapper = orderMapper;
        this.validation = validation;
        this.entityManager = entityManager;
    }


    @Override
    public List<FilterOrderResponseDTO> findAllOrdersByJobName(String jobName) {
        if (jobService.findByName(jobName).isEmpty())
            throw new JobIsNotExistException("this job does not exist!");
        List<Order> orders = repository.findByJobNameAndOrderStatus(jobName,
                WAITING_FOR_WORKER_SUGGESTION,
                WAITING_FOR_WORKER_SELECTION);
        List<FilterOrderResponseDTO> orDTOS = new ArrayList<>();
        orders.forEach(o -> orDTOS.add(orderMapper.convertToFilterDTO(o)));
        return orDTOS;
    }

    @Override
    public List<LimitedOrderResponseDTO> findAllOrdersByJobNameAndProvince(
            String jobName, String workerProvince) {
        List<Order> dbOrders = repository.findByJobNameAndOrderStatus(jobName,
                WAITING_FOR_WORKER_SUGGESTION,
                WAITING_FOR_WORKER_SELECTION);
        List<LimitedOrderResponseDTO> lorDTOS = new ArrayList<>();
        if (dbOrders.isEmpty())
            return lorDTOS;
        List<Order> relatedOrders = dbOrders.stream().filter(o ->
                o.getAddress().getProvince().equals(workerProvince)).toList();
        if (relatedOrders.isEmpty())
            return lorDTOS;
        relatedOrders.forEach(o ->
                lorDTOS.add(orderMapper.convertToLimitedDto(o))
        );
        return lorDTOS;
    }

    @Override
    public List<FilterOrderResponseDTO> ordersFilter(FilterOrderDTO dto) {
        List<FilterOrderResponseDTO> forDTOS = new ArrayList<>();
        if (dto.equals(null)) return forDTOS;
        List<Long> dbJobsId = new ArrayList<>();
        if (dto.getMainServiceId() != null) {
            Optional<MainService> dbMainService = mainServiceService.findById(dto.getMainServiceId());
            if (dbMainService.isEmpty()) return forDTOS;
            else {
                dbMainService.get().getJobList().forEach(j -> dbJobsId.add(j.getId()));
                if (dto.getJobId() != null)
                    if (!dbJobsId.contains(dto.getJobId())) return forDTOS;
            }
        } else if (dto.getJobId() != null)
            dbJobsId.add(dto.getJobId());
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);
        List<Order> resultList = new ArrayList<>();
        if (dbJobsId.isEmpty()) dbJobsId.add(null);
        dbJobsId.stream().forEach(ji -> {
            dto.setJobId(ji);
            searchFilters(dto, predicateList, resultList, orderCriteriaQuery, criteriaBuilder, orderRoot);
        });
        if (!resultList.isEmpty()) resultList.forEach(rl -> forDTOS.add(orderMapper.convertToFilterDTO(rl)));
        return forDTOS;
    }

    private void searchFilters(FilterOrderDTO dto, List<Predicate> predicateList, List<Order> resultList,
                               CriteriaQuery<Order> orderCriteriaQuery, CriteriaBuilder criteriaBuilder,
                               Root<Order> orderRoot) {
        createFilters(dto, predicateList, criteriaBuilder, orderRoot);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        orderCriteriaQuery.select(orderRoot).where(predicates);
        resultList.addAll(entityManager.createQuery(orderCriteriaQuery).getResultList());
    }

    private void createFilters(FilterOrderDTO orderDTO, List<Predicate> predicateList,
                               CriteriaBuilder criteriaBuilder, Root<Order> orderRoot) {
        if (orderDTO.getDescription() != null) {
            validation.checkText(orderDTO.getDescription());
            String description = "%" + orderDTO.getDescription() + "%";
            predicateList.add(criteriaBuilder.like(orderRoot.get("description"),
                    description));
        }
        if (orderDTO.getOrderStatus() != null) {
            predicateList.add(criteriaBuilder.equal(orderRoot.get("orderStatus"),
                    orderDTO.getOrderStatus()));
        }
        if (orderDTO.getJobId() != null) {
            validation.checkPositiveNumber(orderDTO.getJobId());
            predicateList.add(criteriaBuilder.equal(orderRoot.get("job"),
                    jobService.findById(orderDTO.getJobId()).get()));
        }
        if (orderDTO.getMinProposedPrice() == null && orderDTO.getMaxProposedPrice() != null)
            orderDTO.setMinProposedPrice(0L);
        if (orderDTO.getMinProposedPrice() != null && orderDTO.getMaxProposedPrice() == null)
            orderDTO.setMaxProposedPrice(Long.MAX_VALUE);
        if (orderDTO.getMinProposedPrice() != null && orderDTO.getMaxProposedPrice() != null)
            predicateList.add(criteriaBuilder.between(orderRoot.get("proposedPrice"),
                    orderDTO.getMinProposedPrice(), orderDTO.getMaxProposedPrice()));

        if (orderDTO.getMinOrderRegistrationDate() == null && orderDTO.getMaxOrderRegistrationDate() != null)
            orderDTO.setMinOrderRegistrationDate(LocalDateTime.now().minusYears(5));
        if (orderDTO.getMinOrderRegistrationDate() != null && orderDTO.getMaxOrderRegistrationDate() == null)
            orderDTO.setMaxOrderRegistrationDate(LocalDateTime.now());
        if (orderDTO.getMinOrderRegistrationDate() != null && orderDTO.getMaxOrderRegistrationDate() != null)
            predicateList.add(criteriaBuilder.between(orderRoot.get("registrationTime"),
                    orderDTO.getMinOrderRegistrationDate(),
                    orderDTO.getMaxOrderRegistrationDate()));
    }

    @Override
    public void chooseOffer(Order order, Long offerId) {
        order.getOfferList().forEach(o -> {
            if (o.getId().equals(offerId))
                o.setOfferStatus(ACCEPTED);
            else
                o.setOfferStatus(REJECTED);
        });
        order.setOrderStatus(WAITING_FOR_WORKER_TO_COME);
        repository.save(order);
    }

}
