package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import ir.maktabsharif.homeservicephase2.exception.*;
import ir.maktabsharif.homeservicephase2.repository.WorkerRepository;
import ir.maktabsharif.homeservicephase2.service.OfferService;
import ir.maktabsharif.homeservicephase2.service.OrderService;
import ir.maktabsharif.homeservicephase2.service.WorkerService;
import ir.maktabsharif.homeservicephase2.util.ImageConverter;
import ir.maktabsharif.homeservicephase2.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerServiceImpl extends BaseServiceImpl<Worker, Long, WorkerRepository>
        implements WorkerService {

    private final OfferService OFFER_SERVICE;
    private final OrderService ORDER_SERVICE;

    @Autowired
    public WorkerServiceImpl(WorkerRepository repository, OfferService offerService,
                             OrderService orderService) {
        super(repository);
        OFFER_SERVICE = offerService;
        ORDER_SERVICE = orderService;
    }


    @Override
    public Optional<Worker> findByUsername(String email) {
        Validation.checkEmail(email);
        Optional<Worker> worker = (repository.findByEmail(email));
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        return worker;
    }

    @Override
    public void editPassword(Worker worker, String newPassword) {
        Validation.checkPassword(newPassword);
        findByUsername(worker.getEmail());
        if (worker.getPassword().equals(newPassword))
            throw new DuplicatePasswordException("this password has duplicate!");
        repository.editPassword(worker.getEmail(), newPassword);
    }

    @Override
    public void changeWorkerStatus(String workerUsername, WorkerStatus workerStatus) {
        repository.changeWorkerStatus(workerUsername, workerStatus);
    }

    @Override
    public void signUp(Worker worker, File image) {
        Validation.checkEmail(worker.getEmail());
        Validation.checkText(worker.getFirstname());
        Validation.checkText(worker.getLastname());
        Validation.checkPassword(worker.getPassword());
        Validation.checkImage(image);
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
        Validation.checkPositiveNumber(workerId);
        Validation.checkPositiveNumber(orderId);
        Validation.checkPositiveNumber(offer.getProposedPrice());
        Optional<Worker> worker = repository.findById(workerId);
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        if (!(worker.get().getStatus().equals(WorkerStatus.CONFIRMED)))
            throw new WorkerNoAccessException("the status of expert is not CONFIRMED");
        Optional<Order> order = ORDER_SERVICE.findById(orderId);
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
        OFFER_SERVICE.save(offer);
        if (order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_WORKER_SUGGESTION))
            ORDER_SERVICE.changeOrderStatus(orderId, OrderStatus.WAITING_FOR_WORKER_SUGGESTION,
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
}
