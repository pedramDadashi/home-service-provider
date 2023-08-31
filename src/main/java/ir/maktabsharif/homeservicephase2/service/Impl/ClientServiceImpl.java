package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.offer.OfferStatus;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.exception.*;
import ir.maktabsharif.homeservicephase2.mapper.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientServiceImpl extends BaseServiceImpl<Client, Long, ClientRepository>
        implements ClientService {

    private final MainServiceService mainServiceService;
    private final JobService jobService;
    private final OrderService orderService;
    private final OfferService offerService;
    private final CommentService commentService;
    private final WorkerService workerService;

    private final ClientMapper clientMapper;
    private final MainServiceMapper mainServiceMapper;
    //    private final JobMapper jobMapper;
    private final CommentMapper commentMapper;
    private final OrderMapper orderMapper;
    private final OfferMapper offerMapper;

    private final Validation validation;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ClientServiceImpl(ClientRepository repository, MainServiceService mainServiceService,
                             JobService jobService, OfferService offerService,
                             OrderService orderService, CommentService commentService,
                             WorkerService workerService, ClientMapper clientMapper,
                             MainServiceMapper mainServiceMapper,
            /* JobMapper jobMapper,*/ CommentMapper commentMapper,
                             OrderMapper orderMapper, OfferMapper offerMapper,
                             Validation validation, EntityManager entityManager) {
        super(repository);
        this.mainServiceService = mainServiceService;
        this.jobService = jobService;
        this.offerService = offerService;
        this.orderService = orderService;
        this.commentService = commentService;
        this.workerService = workerService;
        this.clientMapper = clientMapper;
        this.mainServiceMapper = mainServiceMapper;
//        this.jobMapper = jobMapper;
        this.commentMapper = commentMapper;
        this.orderMapper = orderMapper;
        this.offerMapper = offerMapper;
        this.validation = validation;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByUsername(String email) {
        validation.checkEmail(email);
        Optional<Client> client = (repository.findByEmail(email));
        if (client.isEmpty())
            throw new ClientNotExistException("this client does not exist!");
        return client;
    }

    @Override
    public ProjectResponse addClient(UserRegistrationDTO clientRegistrationDTO) {
        validation.checkEmail(clientRegistrationDTO.getEmail());
        if (repository.findByEmail(clientRegistrationDTO.getEmail()).isPresent())
            throw new DuplicateEmailException("this Email already exist!");
        validation.checkText(clientRegistrationDTO.getFirstname());
        validation.checkText(clientRegistrationDTO.getLastname());
        validation.checkPassword(clientRegistrationDTO.getPassword());
        Client client = clientMapper.convertToClient(clientRegistrationDTO);
        repository.save(client);
        return new ProjectResponse("200", "ADDED SUCCESSFUL");
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse loginClient(LoginDTO clientLoginDto) {
        validation.checkEmail(clientLoginDto.getUsername());
        Optional<Client> client = repository.findByEmail(clientLoginDto.getUsername());
        if (client.isEmpty())
            throw new ClientNotExistException("this client does not exist!");
        if (!client.get().getPassword().equals(clientLoginDto.getPassword()))
            throw new PasswordIncorrect("this password incorrect!");
        return new ProjectResponse("200", "LOGIN SUCCESSFUL");
    }

    @Override
    public ProjectResponse editPassword(ChangePasswordDTO changePasswordDTO) {
        LoginDTO loginDTO = new LoginDTO(changePasswordDTO.getUsername(), changePasswordDTO.getPassword());
        loginClient(loginDTO);
        validation.checkPassword(changePasswordDTO.getNewPassword());
        if (changePasswordDTO.getPassword().equals(changePasswordDTO.getNewPassword()))
            throw new DuplicatePasswordException("this new password has duplicate!");
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword()))
            throw new DuplicatePasswordException("this confirm new password not match with new password!");
        Optional<Client> client = repository.findByEmail(changePasswordDTO.getUsername());
        client.get().setPassword(changePasswordDTO.getConfirmNewPassword());
        return new ProjectResponse("200", "CHANGED PASSWORD SUCCESSFUL");
    }

    @Override
    @Transactional(readOnly = true)
    public List<MainServiceResponseDTO> showAllMainServices() {
        List<MainService> mainServices = mainServiceService.findAll();
        List<MainServiceResponseDTO> msDTOS = new ArrayList<>();
        mainServices.forEach(ms -> msDTOS.add(mainServiceMapper.convertToDTO(ms)));
        return msDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> showJobs(Long mainServiceId) {
        validation.checkPositiveNumber(mainServiceId);
        if (mainServiceService.findById(mainServiceId).isEmpty())
            throw new MainServiceIsNotExistException("this main service dose not exist!");
        return jobService.findByMainServiceId(mainServiceId);
    }

    @Override
    public ProjectResponse addNewOrder(SubmitOrderDTO submitOrderDTO) {
        if (submitOrderDTO.getWorkStartDate().isBefore(LocalDateTime.now()))
            throw new TimeException("passed this date!");
        if (submitOrderDTO.getWorkEndDate().isBefore(submitOrderDTO.getWorkStartDate()))
            throw new TimeException("Time does not go back!");
        validation.checkPositiveNumber(submitOrderDTO.getJobId());
        Optional<Job> job = jobService.findById(submitOrderDTO.getJobId());
        if (job.isEmpty())
            throw new JobIsNotExistException("this job does not exist!");
        validation.checkPositiveNumber(submitOrderDTO.getClientProposedPrice());
        if (job.get().getBasePrice() >= submitOrderDTO.getClientProposedPrice())
            throw new AmountLessExseption("this proposed price is less than base price of the job!");
        validation.checkPositiveNumber(submitOrderDTO.getClientId());
        Optional<Client> client = repository.findById(submitOrderDTO.getClientId());
        if (client.isEmpty())
            throw new ClientNotExistException("this client does not exist!");
        validation.checkBlank(submitOrderDTO.getAddress());
        validation.checkBlank(submitOrderDTO.getDescription());
        Order order = new Order(submitOrderDTO.getClientProposedPrice(),
                submitOrderDTO.getDescription(), submitOrderDTO.getWorkStartDate(),
                submitOrderDTO.getAddress(), submitOrderDTO.getWorkEndDate(),
                client.get(), job.get());
        orderService.save(order);
        return new ProjectResponse("200", "ADDED SUCCESSFUL");
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> showAllOrders(Long clientId) {
        validation.checkPositiveNumber(clientId);
        Optional<Client> client = repository.findById(clientId);
        if (client.isEmpty())
            throw new ClientNotExistException("this client does not exist!");
        List<OrderResponseDTO> orDTS = new ArrayList<>();
        client.get().getOrderList().forEach(o -> orDTS.add(orderMapper.convertToDTO(o)));
        return orDTS;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getWorkerCredit(Long workerId) {
        validation.checkPositiveNumber(workerId);
        Optional<Client> client = repository.findById(workerId);
        if (client.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        return client.get().getCredit();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponseDTO> findOfferListByOrderIdBasedOnProposedPrice(Long orderId) {
        validation.checkPositiveNumber(orderId);
        if (orderService.findById(orderId).isEmpty())
            throw new ClientNotExistException("this order does not exist!");
        List<OfferResponseDTO> orDTOS = new ArrayList<>();
        offerService.findOfferListByOrderIdBasedOnProposedPrice(orderId).forEach(o -> orDTOS.add(offerMapper.convertToDTO(o)));
        return orDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponseDTO> findOfferListByOrderIdBasedOnWorkerScore(Long orderId) {
        validation.checkPositiveNumber(orderId);
        if (orderService.findById(orderId).isEmpty())
            throw new ClientNotExistException("this order does not exist!");
        List<OfferResponseDTO> orDTOS = new ArrayList<>();
        offerService.findOfferListByOrderIdBasedOnWorkerScore(orderId).
                forEach(o -> orDTOS.add(offerMapper.convertToDTO(o)));
        return orDTOS;
    }

    @Override
    public ProjectResponse choseWorkerForOrder(Long offerId) {
        validation.checkPositiveNumber(offerId);
        Optional<Offer> offer = offerService.findById(offerId);
        if (offer.isEmpty())
            throw new OfferNotExistException("this offer does not exist");
        Order order = offer.get().getOrder();
        order.getOfferList().forEach(o -> {
            if (o.equals(offer.get()) && o.getOfferStatus().equals(OfferStatus.WAITING)) {
                o.setOfferStatus(OfferStatus.ACCEPTED);
                order.setExecutionTime(o.getExecutionTime());
            } else {
                o.setOfferStatus(OfferStatus.REJECTED);
            }
        });
        order.setOrderStatus(OrderStatus.WAITING_FOR_WORKER_TO_COME);
        orderService.save(order);
        return new ProjectResponse("200", "CHANGED SUCCESSFUL");
    }

    @Override
    public ProjectResponse changeOrderStatusToStarted(Long orderId) {
        validation.checkPositiveNumber(orderId);
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderIsNotExistException("this order does not exist!");
        if (!order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_WORKER_TO_COME))
            throw new OrderIsNotExistException
                    ("the status of this order is not yet \"WAITING FOR EXPERT TO COME\"!");
        order.get().getOfferList().forEach(o -> {
            if (o.getOfferStatus().equals(OfferStatus.ACCEPTED)) {
                if (o.getExecutionTime().isBefore(LocalDateTime.now()))
                    throw new TimeException("the worker has not arrived at your place yet!");
            }
        });
        order.get().setOrderStatus(OrderStatus.STARTED);
        orderService.save(order.get());
        return new ProjectResponse("200", "CHANGED SUCCESSFUL");
    }

    @Override
    public ProjectResponse changeOrderStatusToDone(Long orderId) {
        validation.checkPositiveNumber(orderId);
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderIsNotExistException("this order does not exist!");
        if (!order.get().getOrderStatus().equals(OrderStatus.STARTED))
            throw new OrderIsNotExistException
                    ("the status of this order is not yet \"STARTED\"!");
        final Long[] offerId = new Long[1];
        order.get().getOfferList().forEach(o -> {
            if (o.getOfferStatus().equals(OfferStatus.ACCEPTED))
                offerId[0] = o.getId();
//                if (o.getEndTime().isBefore(LocalDateTime.now()))
//                    throw new TimeException("the work of the worker in your place not finished yet!");
        });
        order.get().setOrderStatus(OrderStatus.DONE);
        orderService.save(order.get());
        Optional<Offer> offer = offerService.findById(offerId[0]);
        int delay = (LocalDateTime.now().getHour()) - (offer.get().getEndTime().getHour());
        if (delay > 0) {
            Worker worker = offer.get().getWorker();
            double workerScore = worker.getScore();
            worker.setScore(workerScore - delay);
            workerService.save(worker);
        }
        return new ProjectResponse("200", "CHANGED SUCCESSFUL");
    }

    @Override
    public ProjectResponse addComment(CommentRequestDTO commentRequestDTO) {
        validation.checkPositiveNumber(commentRequestDTO.getOrderId());
        validation.checkScore(commentRequestDTO.getScore());
        if (!commentRequestDTO.getComment().isEmpty())
            validation.checkText(commentRequestDTO.getComment());
        else
            commentRequestDTO.setComment("I have no idea!");
        Optional<Order> order = orderService.findById(commentRequestDTO.getOrderId());
        if (order.isEmpty())
            throw new OrderIsNotExistException("this order does not exist!");
        order.get().getOfferList().forEach(o -> {
            if (o.getOfferStatus().equals(OfferStatus.ACCEPTED)) {
                o.getWorker().rate(commentRequestDTO.getScore());
            }
        });
        commentService.save(commentMapper.convertToComment(commentRequestDTO));
        return new ProjectResponse("200", "ADDED SUCCESSFUL");
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
