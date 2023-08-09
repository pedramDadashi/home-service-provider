package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import ir.maktabsharif.homeservicephase2.exception.*;
import ir.maktabsharif.homeservicephase2.repository.ClientRepository;
import ir.maktabsharif.homeservicephase2.service.*;
import ir.maktabsharif.homeservicephase2.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl extends BaseServiceImpl<Client, Long, ClientRepository>
        implements ClientService {

    private final MainServiceService MAIN_SERVICE_SERVICE;
    private final JobService JOB_SERVICE;
    private final OfferService OFFER_SERVICE;
    private final OrderService ORDER_SERVICE;

    @Autowired
    public ClientServiceImpl(ClientRepository repository, MainServiceService MAIN_SERVICE_SERVICE,
                             JobService JOB_SERVICE, OfferService OFFER_SERVICE,
                             OrderService ORDER_SERVICE) {
        super(repository);
        this.MAIN_SERVICE_SERVICE = MAIN_SERVICE_SERVICE;
        this.JOB_SERVICE = JOB_SERVICE;
        this.OFFER_SERVICE = OFFER_SERVICE;
        this.ORDER_SERVICE = ORDER_SERVICE;
    }

    @Override
    public Optional<Client> findByUsername(String email) {
        Validation.checkEmail(email);
        Optional<Client> client = (repository.findByEmail(email));
        if (client.isEmpty())
            throw new ClientNotExistException("this client does not exist!");
        return client;
    }

    @Override
    public void editPassword(Client client, String newPassword) {
        Validation.checkPassword(newPassword);
        findByUsername(client.getEmail());
        if (client.getPassword().equals(newPassword))
            throw new DuplicatePasswordException("this password has duplicate!");
        repository.editPassword(client.getEmail(), newPassword);
    }

    @Override
    public void signUp(Client client) {
        Validation.checkEmail(client.getEmail());
        Validation.checkText(client.getFirstname());
        Validation.checkText(client.getLastname());
        if (repository.findByEmail(client.getEmail()).isPresent())
            throw new DuplicateEmailException("this Email already exist!");
        Validation.checkPassword(client.getPassword());
        repository.save(client);
    }

    @Override
    public List<MainService> findAllMainService() {
        return MAIN_SERVICE_SERVICE.findAll();
    }

    @Override
    public List<Job> findAllJob() {
        return JOB_SERVICE.findAll();
    }

    @Override
    public void addOrder(Client client, String jobName, Long proposedPrice,
                         String description, LocalDateTime executionTime,
                         LocalDateTime updateTime, String address) {
        Validation.checkPositiveNumber(proposedPrice);
        Validation.checkText(jobName);
        Validation.checkBlank(description);
        Validation.checkBlank(address);
        if (executionTime.isBefore(LocalDateTime.now()))
            throw new TimeException("passed this date!");
        if (updateTime.isBefore(executionTime))
            throw new TimeException("Time does not go back!");
        Optional<Job> job = JOB_SERVICE.findByName(jobName);
        if (job.isEmpty())
            throw new JobIsNotExistException("this job does not exist!");
        if (job.get().getBasePrice() < proposedPrice)
            throw new AmountLessExseption("this proposed price is less than base price of the job!");
        Optional<Client> client1 = findByUsername(client.getEmail());
        if (client1.isEmpty())
            throw new ClientNotExistException("this client does not exist!");
        Order order = new Order(proposedPrice, description,
                executionTime, address, updateTime,
                client1.get(), job.get());
        ORDER_SERVICE.save(order);
    }

    @Override
    public List<Offer> findOfferListByOrderIdBasedOnProposedPrice(Long orderId) {
        Validation.checkPositiveNumber(orderId);
        return OFFER_SERVICE.findOfferListByOrderIdBasedOnProposedPrice(orderId);
    }

    @Override
    public List<Offer> findOfferListByOrderIdBasedOnWorkerScore(Long orderId) {
        Validation.checkPositiveNumber(orderId);
        return OFFER_SERVICE.findOfferListByOrderIdBasedOnWorkerScore(orderId);
    }

    @Override
    public void acceptOffer(Long offerId) {
        Validation.checkPositiveNumber(offerId);
        Optional<Offer> offer = OFFER_SERVICE.findById(offerId);
        if (offer.isEmpty())
            throw new OfferNotExistException("this offer does not exist");
        OFFER_SERVICE.editIsAccept(offerId, true);
        Long orderId = offer.get().getOrder().getId();
        ORDER_SERVICE.changeOrderStatus(orderId, OrderStatus.WAITING_FOR_WORKER_SELECTION,
                OrderStatus.WAITING_FOR_WORKER_TO_COME);

    }

    @Override
    public void changeOrderStatusAfterWorkerComes(Long orderId) {
        Validation.checkPositiveNumber(orderId);
        Optional<Order> order = ORDER_SERVICE.findById(orderId);
        if (order.isEmpty())
            throw new OrderIsNotExistException("this order does not exist!");
        if (!order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_WORKER_TO_COME))
            throw new OrderIsNotExistException
                    ("the status of this order is not yet \"WAITING FOR EXPERT TO COME\"!");
        Optional<Offer> offer = OFFER_SERVICE.findOfferByOrderIdAndIsAccept(orderId, true);
        if (offer.isEmpty())
            throw new OfferNotExistException("this offer does not exist");
        if (offer.get().getExecutionTime().isBefore(LocalDateTime.now()))
            throw new TimeException("the worker has not arrived at your place yet!");
        ORDER_SERVICE.changeOrderStatus(orderId, OrderStatus.WAITING_FOR_WORKER_TO_COME,
                OrderStatus.STARTED);
    }

    @Override
    public void changeOrderStatusAfterStarted(Long orderId) {
        Validation.checkPositiveNumber(orderId);
        Optional<Order> order = ORDER_SERVICE.findById(orderId);
        if (order.isEmpty())
            throw new OrderIsNotExistException("this order does not exist!");
        if (!order.get().getOrderStatus().equals(OrderStatus.STARTED))
            throw new OrderIsNotExistException
                    ("the status of this order is not yet \"STARTED\"!");
        Optional<Offer> offer = OFFER_SERVICE.findOfferByOrderIdAndIsAccept(orderId, true);
        if (offer.isEmpty())
            throw new OfferNotExistException("this offer does not exist");
        if (offer.get().getEndTime().isBefore(LocalDateTime.now()))
            throw new TimeException("the work of the worker in your place not finished yet!");
        ORDER_SERVICE.changeOrderStatus(orderId, OrderStatus.STARTED,
                OrderStatus.DONE);
    }
}
