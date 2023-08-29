package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.request.FilterClientDTO;
import ir.maktabsharif.homeservicephase2.dto.response.FilterClientResponseDTO;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import ir.maktabsharif.homeservicephase2.exception.*;
import ir.maktabsharif.homeservicephase2.mapper.ClientMapper;
import ir.maktabsharif.homeservicephase2.repository.ClientRepository;
import ir.maktabsharif.homeservicephase2.service.*;
import ir.maktabsharif.homeservicephase2.util.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl extends BaseServiceImpl<Client, Long, ClientRepository>
        implements ClientService {

    private final MainServiceService MAIN_SERVICE_SERVICE;
    private final JobService JOB_SERVICE;
    private final OfferService OFFER_SERVICE;
    private final OrderService ORDER_SERVICE;

    private final ClientMapper clientMapper;

    private final Validation validation;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ClientServiceImpl(ClientRepository repository, MainServiceService MAIN_SERVICE_SERVICE,
                             JobService JOB_SERVICE, OfferService OFFER_SERVICE,
                             OrderService ORDER_SERVICE, ClientMapper clientMapper,
                             Validation validation, EntityManager entityManager) {
        super(repository);
        this.MAIN_SERVICE_SERVICE = MAIN_SERVICE_SERVICE;
        this.JOB_SERVICE = JOB_SERVICE;
        this.OFFER_SERVICE = OFFER_SERVICE;
        this.ORDER_SERVICE = ORDER_SERVICE;
        this.clientMapper = clientMapper;
        this.validation = validation;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Client> findByUsername(String email) {
        validation.checkEmail(email);
        Optional<Client> client = (repository.findByEmail(email));
        if (client.isEmpty())
            throw new ClientNotExistException("this client does not exist!");
        return client;
    }

    @Override
    public void editPassword(Client client, String newPassword) {
        validation.checkPassword(newPassword);
        Optional<Client> client1 = findByUsername(client.getEmail());
        if (client.getPassword().equals(newPassword))
            throw new DuplicatePasswordException("this password has duplicate!");
        client1.get().setPassword(newPassword);
        repository.save(client1.get());
    }

    @Override
    public void signUp(Client client) {
        validation.checkEmail(client.getEmail());
        validation.checkText(client.getFirstname());
        validation.checkText(client.getLastname());
        if (repository.findByEmail(client.getEmail()).isPresent())
            throw new DuplicateEmailException("this Email already exist!");
        validation.checkPassword(client.getPassword());
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
        validation.checkPositiveNumber(proposedPrice);
        validation.checkText(jobName);
        validation.checkBlank(description);
        validation.checkBlank(address);
        if (executionTime.isBefore(LocalDateTime.now()))
            throw new TimeException("passed this date!");
        if (updateTime.isBefore(executionTime))
            throw new TimeException("Time does not go back!");
        Optional<Job> job = JOB_SERVICE.findByName(jobName);
        if (job.isEmpty())
            throw new JobIsNotExistException("this job does not exist!");
        if (job.get().getBasePrice() >= proposedPrice)
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
        validation.checkPositiveNumber(orderId);
        return OFFER_SERVICE.findOfferListByOrderIdBasedOnProposedPrice(orderId);
    }

    @Override
    public List<Offer> findOfferListByOrderIdBasedOnWorkerScore(Long orderId) {
        validation.checkPositiveNumber(orderId);
        return OFFER_SERVICE.findOfferListByOrderIdBasedOnWorkerScore(orderId);
    }

    @Override
    public void acceptOffer(Long offerId) {
        validation.checkPositiveNumber(offerId);
        Optional<Offer> offer = OFFER_SERVICE.findById(offerId);
        if (offer.isEmpty())
            throw new OfferNotExistException("this offer does not exist");
        OFFER_SERVICE.editIsAccept(offerId, true);
        Long orderId = offer.get().getOrder().getId();
        ORDER_SERVICE.changeOrderStatus(orderId,
                OrderStatus.WAITING_FOR_WORKER_TO_COME);

    }

    @Override
    public void changeOrderStatusAfterWorkerComes(Long orderId) {
        validation.checkPositiveNumber(orderId);
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
        ORDER_SERVICE.changeOrderStatus(orderId,
                OrderStatus.STARTED);
    }

    @Override
    public void changeOrderStatusAfterStarted(Long orderId) {
        validation.checkPositiveNumber(orderId);
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
        ORDER_SERVICE.changeOrderStatus(orderId,
                OrderStatus.DONE);
    }

    @Override
    public List<FilterClientResponseDTO> clientFilter(FilterClientDTO clientDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> clientCriteriaQuery = criteriaBuilder.createQuery(Client.class);
        Root<Client> clientRoot = clientCriteriaQuery.from(Client.class);

        createFilters(clientDTO, predicateList, criteriaBuilder, clientRoot);

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        clientCriteriaQuery.select(clientRoot).where(predicates);

        List<Client> resultList = entityManager.createQuery(clientCriteriaQuery).getResultList();
        List<FilterClientResponseDTO> fcDTOS = new ArrayList<>();
        resultList.forEach(rl -> fcDTOS.add(clientMapper.convertToFilterDTO(rl)));
        return fcDTOS;
    }

    private void createFilters(FilterClientDTO clientDTO, List<Predicate> predicateList,
                               CriteriaBuilder criteriaBuilder, Root<Client> clientRoot) {
        if (clientDTO.getFirstname() != null) {
            validation.checkText(clientDTO.getFirstname());
            String firstname = "%" + clientDTO.getFirstname() + "%";
            predicateList.add(criteriaBuilder.like(clientRoot.get("firstname"), firstname));
        }
        if (clientDTO.getLastname() != null) {
            validation.checkText(clientDTO.getLastname());
            String lastname = "%" + clientDTO.getLastname() + "%";
            predicateList.add(criteriaBuilder.like(clientRoot.get("lastname"), lastname));
        }
        if (clientDTO.getEmail() != null) {
            validation.checkEmail(clientDTO.getEmail());
            String email = "%" + clientDTO.getEmail() + "%";
            predicateList.add(criteriaBuilder.like(clientRoot.get("email"), email));
        }
        if (clientDTO.getCredit() != null) {
            validation.checkPositiveNumber(clientDTO.getCredit());
            predicateList.add(criteriaBuilder.gt(clientRoot.get("credit"), clientDTO.getCredit()));
        }
    }
}
