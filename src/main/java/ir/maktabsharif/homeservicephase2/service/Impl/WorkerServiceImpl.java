package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.request.FilterWorkerDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterWorkerResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import ir.maktabsharif.homeservicephase2.exception.*;
import ir.maktabsharif.homeservicephase2.mapper.WorkerMapper;
import ir.maktabsharif.homeservicephase2.repository.WorkerRepository;
import ir.maktabsharif.homeservicephase2.service.OfferService;
import ir.maktabsharif.homeservicephase2.service.OrderService;
import ir.maktabsharif.homeservicephase2.service.WorkerService;
import ir.maktabsharif.homeservicephase2.util.ImageConverter;
import ir.maktabsharif.homeservicephase2.util.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkerServiceImpl extends BaseServiceImpl<Worker, Long, WorkerRepository>
        implements WorkerService {

    private final OfferService offerService;
    private final OrderService orderService;

    private final WorkerMapper workerMapper;

    private final Validation validation;

    @PersistenceContext
    private EntityManager entityManager;

    public WorkerServiceImpl(WorkerRepository repository, OfferService offerService,
                             OrderService orderService, WorkerMapper workerMapper,
                             Validation validation, EntityManager entityManager) {
        super(repository);
        this.offerService = offerService;
        this.orderService = orderService;
        this.workerMapper = workerMapper;
        this.validation = validation;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Worker> findByUsername(String email) {
        validation.checkEmail(email);
        Optional<Worker> worker = (repository.findByEmail(email));
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        return worker;
    }

    @Override
    public void editPassword(Worker worker, String newPassword) {
        validation.checkPassword(newPassword);
        findByUsername(worker.getEmail());
        if (worker.getPassword().equals(newPassword))
            throw new DuplicatePasswordException("this password has duplicate!");
        repository.editPassword(worker.getEmail(), newPassword);
    }

    @Override
    public void changeWorkerStatus(String workerUsername, WorkerStatus workerStatus) {
        Optional<Worker> worker = repository.findByEmail(workerUsername);
        worker.get().setStatus(workerStatus);
        repository.save(worker.get());
    }

    @Override
    public void signUp(Worker worker, File image) {
        validation.checkEmail(worker.getEmail());
        validation.checkText(worker.getFirstname());
        validation.checkText(worker.getLastname());
        validation.checkPassword(worker.getPassword());
        validation.checkImage(image);
        if (repository.findByEmail(worker.getEmail()).isPresent())
            throw new DuplicateEmailException("this Email already exist!");
        String stringImage;
        try {
            stringImage = ImageConverter.getStringImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        worker.setImage(stringImage);
        repository.save(worker);
    }

    @Override
    public void createOfferForOrder(Long workerId, Long orderId, Offer offer) {
        validation.checkPositiveNumber(workerId);
        validation.checkPositiveNumber(orderId);
        validation.checkPositiveNumber(offer.getProposedPrice());
        Optional<Worker> worker = repository.findById(workerId);
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        if (!(worker.get().getStatus().equals(WorkerStatus.CONFIRMED)))
            throw new WorkerNoAccessException("the status of expert is not CONFIRMED");
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderIsNotExistException("this order does not exist!");
        if (!(worker.get().getJobSet().contains(order.get().getJob())))
            throw new WorkerNoAccessException("this worker does not have such job!");
        if (offer.getEndTime().isBefore(offer.getExecutionTime()))
            throw new TimeException("time does not go back!");
        if (offer.getExecutionTime().isBefore(order.get().getExecutionTime()))
            throw new TimeException("no order has been in your proposed time for begin job!");
        if (order.get().getJob().getBasePrice() > offer.getProposedPrice())
            throw new AmountLessExseption("the proposed-price should not be lower than the base-price!");
        if (!(order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_WORKER_SUGGESTION) ||
              order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_WORKER_SELECTION)))
            throw new OrderStatusException("the status of this order not" +
                                           " \"WAITING FOR EXPERT SUGGESTION\" or" +
                                           " \"WAITING FOR EXPERT SELECTION\"!");
        offer.setWorker(worker.get());
        offer.setOrder(order.get());
        offerService.save(offer);
        if (order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_WORKER_SUGGESTION))
            orderService.changeOrderStatus(orderId,
                    OrderStatus.WAITING_FOR_WORKER_SELECTION);
    }

    @Override
    public boolean isExistByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public List<Worker> findAll() {
        List<Worker> workerList = repository.findAll();
        if (workerList.isEmpty())
            throw new WorkerIsNotExistException("there are no workers!");
        return workerList;
    }

    @Override
    public List<FilterWorkerResponseDTO> workerFilter(FilterWorkerDTO workerDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Worker> workerCriteriaQuery = criteriaBuilder.createQuery(Worker.class);
        Root<Worker> workerRoot = workerCriteriaQuery.from(Worker.class);

        createFilters(workerDTO, predicateList, criteriaBuilder, workerRoot);

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        workerCriteriaQuery.select(workerRoot).where(predicates);

        List<Worker> resultList = entityManager.createQuery(workerCriteriaQuery).getResultList();
        List<FilterWorkerResponseDTO> fwDTOS = new ArrayList<>();
        resultList.forEach(rl -> fwDTOS.add(workerMapper.convertToFilterDTO(rl)));
        return fwDTOS;
    }

    private void createFilters(FilterWorkerDTO workerDTO, List<Predicate> predicateList,
                               CriteriaBuilder criteriaBuilder, Root<Worker> workerRoot) {
        if (workerDTO.getFirstname() != null) {
            validation.checkText(workerDTO.getFirstname());
            String firstname = "%" + workerDTO.getFirstname() + "%";
            predicateList.add(criteriaBuilder.like(workerRoot.get("firstname"), firstname));
        }
        if (workerDTO.getLastname() != null) {
            validation.checkText(workerDTO.getLastname());
            String lastname = "%" + workerDTO.getLastname() + "%";
            predicateList.add(criteriaBuilder.like(workerRoot.get("lastname"), lastname));
        }
        if (workerDTO.getEmail() != null) {
            validation.checkEmail(workerDTO.getEmail());
            String email = "%" + workerDTO.getEmail() + "%";
            predicateList.add(criteriaBuilder.like(workerRoot.get("email"), email));
        }
        if (workerDTO.getIsActive() != null) {
            predicateList.add(criteriaBuilder.equal(workerRoot.get("isActive"), workerDTO.getIsActive()));
        }
        if (workerDTO.getWorkerStatus() != null) {
            predicateList.add(criteriaBuilder.equal(workerRoot.get("workerStatus"), workerDTO.getWorkerStatus()));
        }
        if (workerDTO.getRate() != null) {
            if (workerDTO.getRate() <= 0 || workerDTO.getRate() > 5)
                throw new ScoreOutOfBoundsException("this rate is out of range");
            predicateList.add(criteriaBuilder.lt(workerRoot.get("rate"), workerDTO.getRate()));
        }
        if (workerDTO.getCredit() != null) {
            validation.checkPositiveNumber(workerDTO.getCredit());
            predicateList.add(criteriaBuilder.gt(workerRoot.get("credit"), workerDTO.getCredit()));
        }
    }

}
