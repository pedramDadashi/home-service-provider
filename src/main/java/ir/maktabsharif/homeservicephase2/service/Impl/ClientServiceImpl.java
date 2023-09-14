package ir.maktabsharif.homeservicephase2.service.Impl;

import cn.apiclub.captcha.Captcha;
import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.Address.Address;
import ir.maktabsharif.homeservicephase2.entity.comment.Comment;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.service.Job;
import ir.maktabsharif.homeservicephase2.entity.user.Admin;
import ir.maktabsharif.homeservicephase2.entity.user.Client;
import ir.maktabsharif.homeservicephase2.entity.user.Users;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.exception.*;
import ir.maktabsharif.homeservicephase2.mapper.*;
import ir.maktabsharif.homeservicephase2.repository.ClientRepository;
import ir.maktabsharif.homeservicephase2.security.token.entity.Token;
import ir.maktabsharif.homeservicephase2.security.token.service.TokenService;
import ir.maktabsharif.homeservicephase2.service.*;
import ir.maktabsharif.homeservicephase2.util.CaptchaUtil;
import ir.maktabsharif.homeservicephase2.util.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ir.maktabsharif.homeservicephase2.entity.offer.enums.OfferStatus.ACCEPTED;
import static ir.maktabsharif.homeservicephase2.entity.offer.enums.OfferStatus.REJECTED;
import static ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus.*;
import static ir.maktabsharif.homeservicephase2.entity.user.enums.ClientStatus.*;

@Service
@Transactional
public class ClientServiceImpl extends BaseServiceImpl<Client, Long, ClientRepository>
        implements ClientService {
    private static final String PRE_WRITTEN_MESSAGE =
            "This is a pre-written message: I have no particular opinion.";

    private final MainServiceService mainServiceService;
    private final JobService jobService;
    private final OrderService orderService;
    private final OfferService offerService;
    private final CommentService commentService;
    private final WorkerService workerService;
    private final ManagerService managerService;
    private final TokenService tokenService;
    private final EmailService emailService;

    private final ClientMapper clientMapper;
    private final MainServiceMapper mainServiceMapper;
    private final CommentMapper commentMapper;
    private final OrderMapper orderMapper;
    private final OfferMapper offerMapper;
    private final AddressMapper addressMapper;

    private final Validation validation;
    private final PasswordEncoder passwordEncoder;

    private EntityManager entityManager;

    @Autowired
    public ClientServiceImpl(ClientRepository repository, MainServiceService mainServiceService,
                             JobService jobService, OfferService offerService,
                             OrderService orderService, CommentService commentService,
                             WorkerService workerService,
                             ManagerService managerService, TokenService tokenService,
                             EmailService emailService, OfferMapper offerMapper,
                             ClientMapper clientMapper, MainServiceMapper mainServiceMapper,
                             CommentMapper commentMapper, OrderMapper orderMapper,
                             AddressMapper addressMapper, Validation validation,
                             PasswordEncoder passwordEncoder, EntityManager entityManager) {
        super(repository);
        this.mainServiceService = mainServiceService;
        this.jobService = jobService;
        this.offerService = offerService;
        this.orderService = orderService;
        this.commentService = commentService;
        this.workerService = workerService;
        this.managerService = managerService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.clientMapper = clientMapper;
        this.mainServiceMapper = mainServiceMapper;
        this.commentMapper = commentMapper;
        this.orderMapper = orderMapper;
        this.offerMapper = offerMapper;
        this.addressMapper = addressMapper;
        this.validation = validation;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Client> findByUsername(String email) {
        return repository.findByEmail(email);
    }


    @Override
    public String addNewClient(ClientRegistrationDTO dto) {
        validation.checkEmail(dto.getEmail());
        if (repository.findByEmail(dto.getEmail()).isPresent())
            throw new DuplicateEmailException("this Email already exist!");
        validation.checkPassword(dto.getPassword());
        validation.checkText(dto.getFirstname());
        validation.checkText(dto.getLastname());
        Client client = clientMapper.convertToNewClient(dto);
        repository.save(client);
        String newToken = UUID.randomUUID().toString();
        Token token = new Token(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), client);
        token.setToken(newToken);
        tokenService.saveToken(token);
        SimpleMailMessage mailMessage =
                emailService.createEmail(client.getEmail(), client.getFirstname(), token.getToken(), client.getRole());
        emailService.sendEmail(mailMessage);
        return newToken;
    }

    @Override
    public ProjectResponse editPassword(ChangePasswordDTO changePasswordDTO, Long clientId) {
        validation.checkPassword(changePasswordDTO.getNewPassword());
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword()))
            throw new DuplicatePasswordException("this confirmNewPassword not match with newPassword!");
        Optional<Client> client = repository.findById(clientId);
        client.get().setPassword(passwordEncoder.encode(changePasswordDTO.getConfirmNewPassword()));
        return new ProjectResponse("200", "CHANGE PASSWORD SUCCESSFULLY");
    }

    @Override
    @Transactional(readOnly = true)
    public List<MainServiceResponseDTO> showAllMainServices() {
        List<MainServiceResponseDTO> msrDTOS = new ArrayList<>();
        mainServiceService.findAll().forEach(ms -> msrDTOS.add(mainServiceMapper.convertToDTO(ms)));
        return msrDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> showJobs(String mainServiceName) {
        validation.checkBlank(mainServiceName);
        if (mainServiceService.findByName(mainServiceName).isEmpty())
            throw new MainServiceIsNotExistException("this main service dose not exist!");
        return jobService.findByMainServiceName(mainServiceName);
    }

    @Override
    public ProjectResponse addNewOrder(SubmitOrderDTO soDTO, Long clientId) {
        Client dbClient = repository.findById(clientId).get();
        if (dbClient.getClientStatus().equals(NEW))
            throw new ClientStatusException("you can't sumbit order," +
                                            "because your account is NEW," +
                                            " please added Address and update your account.");
        validation.checkBlank(soDTO.getAddressTitle());
        if ((dbClient.getAddressList().stream().filter(a ->
                a.getTitle().equals(soDTO.getAddressTitle()))).findFirst().isEmpty())
            throw new AddressFormatException("you did not added such an address!");
        if (soDTO.getWorkStartDate().isBefore(LocalDateTime.now()))
            throw new TimeException("passed this date!");
        if (soDTO.getWorkEndDate().isBefore(soDTO.getWorkStartDate()))
            throw new TimeException("Time does not go back!");
        validation.checkBlank(soDTO.getJobName());
        validation.checkPositiveNumber(soDTO.getProposedPrice());
        validation.checkBlank(soDTO.getDescription());
        Optional<Job> job = jobService.findByName(soDTO.getJobName());
        if (job.isEmpty())
            throw new JobIsNotExistException("this job does not exist!");
        if (job.get().getBasePrice() > soDTO.getProposedPrice())
            throw new AmountLessExseption("this proposed price is less than base price of the job!");
        Order order = orderMapper.convertToNewOrder(soDTO, dbClient, job.get());
        if (dbClient.getClientStatus().equals(HAS_NOT_ORDER_YET))
            dbClient.setClientStatus(HAS_ORDERS);
        dbClient.setNumberOfOperation(dbClient.getNumberOfOperation() + 1);
        orderService.save(order);
        return new ProjectResponse("200", "THE ORDER HAS BEEN ADDED SUCCESSFULLY");
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilterOrderResponseDTO> showAllOrders(Long clientId) {
        List<FilterOrderResponseDTO> orDTS = new ArrayList<>();
        List<Order> orderList = repository.findById(clientId).get().getOrderList();
        if (orderList.isEmpty())
            return orDTS;
        orderList.forEach(o -> orDTS.add(orderMapper.convertToFilterDTO(o)));
        return orDTS;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getClientCredit(Long clientId) {
        Optional<Client> client = repository.findById(clientId);
        return client.get().getCredit();
    }

    @Override
    public List<FilterOrderResponseDTO> filterOrder(String orderStatus, Long clientId) {
        Optional<Client> client = repository.findById(clientId);
        List<Order> dbOrderList = client.get().getOrderList();
        List<FilterOrderResponseDTO> orDTO = new ArrayList<>();
        if (dbOrderList.isEmpty())
            return orDTO;
        List<Order> orderList = dbOrderList.stream().filter(o ->
                o.getOrderStatus().name().equals(orderStatus)).toList();
        if (orderList.isEmpty())
            return orDTO;
        orderList.forEach(o ->
                orDTO.add(orderMapper.convertToFilterDTO(o)));
        return orDTO;
    }

    @Override
    public ProjectResponse changeOrderStatusToPaidByOnlinePayment(ClientIdOrderIdDTO dto) {
        validation.checkPositiveNumber(dto.getClientId());
        validation.checkPositiveNumber(dto.getOrderId());
        Optional<Client> dbClient = repository.findById(dto.getClientId());
        if (dbClient.isEmpty())
            throw new ClientNotExistException("not found user");
        Optional<Order> order = orderService.findById(dto.getOrderId());
        if (order.isEmpty())
            throw new OrderIsNotExistException("ont found order");
        accounting(order.get());
        dbClient.get().setPaidCounter(dbClient.get().getPaidCounter() + 1);
        repository.save(dbClient.get());
        return new ProjectResponse("200", "payment was successfully");
    }

    @Override
    public ProjectResponse increaseClientCredit(ClientIdPriceDTO dto) {
        validation.checkPositiveNumber(dto.getClientId());
        validation.checkPositiveNumber(dto.getPrice());
        Optional<Client> dbClient = repository.findById(dto.getClientId());
        if (dbClient.isEmpty())
            throw new ClientNotExistException("not found user");
        dbClient.get().setCredit(dbClient.get().getCredit() + dto.getPrice());
        dbClient.get().setNumberOfOperation(dbClient.get().getNumberOfOperation() + 1);
        repository.save(dbClient.get());
        return new ProjectResponse("200", "your account credit has been successfully " +
                                          "increased by $" + dto.getPrice() + ".");
    }

    @Override
    public ProjectResponse paidByInAppCredit(Long orderId, Users client) {
        validation.checkPositiveNumber(orderId);
        validation.checkOwnerOfTheOrder(orderId, (Client) client);
        Optional<Client> dbClient = repository.findById(client.getId());
        Optional<Order> order = orderService.findById(orderId);
        Long credit = dbClient.get().getCredit();
        Long offerPrice = paymentPriceCalculator(orderId);
        if (credit < offerPrice)
            throw new AmountLessExseption("not enough credit to pay in app");
        client.setCredit(credit - offerPrice);
        accounting(order.get());
        dbClient.get().setPaidCounter(dbClient.get().getPaidCounter() + 1);
        repository.save(dbClient.get());
        return new ProjectResponse("200", "payment was successful");
    }

    private void accounting(Order order) {
        /* ------------------------------------------------- *
         * Assuming the existence of the MANAGER in database *
         * ------------------------------------------------- */
        order.setOrderStatus(PAID);
        Offer offer = order.getOfferList().stream().filter(o ->
                o.getOfferStatus().equals(ACCEPTED)).findFirst().get();
        orderService.save(order);
        Long offerPrice = offer.getProposedPrice();
        Worker worker = offer.getWorker();
        Long managerShare = Math.round(offerPrice * 0.3);
        worker.setCredit(worker.getCredit() + offerPrice - managerShare);
        worker.setPaidCounter(worker.getPaidCounter() + 1);
        workerService.save(worker);
        Admin manager = managerService.findManager();
        manager.setCredit(manager.getCredit() + managerShare);
        managerService.save(manager);
    }

    @Override
    public ModelAndView increaseAccountBalance(Long price, Long clientId, Model model) {
        validation.checkPositiveNumber(price);
        BalancePageDTO balancePageDTO = new BalancePageDTO();
        balancePageDTO.setClientId(clientId);
        balancePageDTO.setPrice(price);
        setupCaptcha(balancePageDTO);
        model.addAttribute("bdto", balancePageDTO);
        return new ModelAndView("incBalance");
    }

    @Override
    public ModelAndView payByOnlinePayment(Long orderId, Users users, Model model) {
        validation.checkPositiveNumber(orderId);
        validation.checkOwnerOfTheOrder(orderId, (Client) users);
        PaymentPageDTO paymentPageDTO = new PaymentPageDTO();
        ClientIdOrderIdDTO clientIdOrderIdDTO = new ClientIdOrderIdDTO(users.getId(), orderId);
        paymentPageDTO.setClientIdOrderIdDTO(clientIdOrderIdDTO);
        paymentPageDTO.setPrice(paymentPriceCalculator(orderId));
        setupCaptcha(paymentPageDTO);
        model.addAttribute("dto", paymentPageDTO);
        return new ModelAndView("payment");
    }

    private void setupCaptcha(PaymentPageDTO dto) {
        Captcha captcha = CaptchaUtil.createCaptcha(350, 100);
        dto.setHidden(captcha.getAnswer());
        dto.setCaptcha("");
        dto.setImage(CaptchaUtil.encodeBase64(captcha));
    }

    private void setupCaptcha(BalancePageDTO dto) {
        Captcha captcha = CaptchaUtil.createCaptcha(350, 100);
        dto.setHidden(captcha.getAnswer());
        dto.setCaptcha("");
        dto.setImage(CaptchaUtil.encodeBase64(captcha));
    }

    @Override
    public ProjectResponse addAddress(AddressDTO addressDTO, Long clientId) {
        validation.checkAddress(addressDTO);
        Optional<Client> client = repository.findById(clientId);
        Address address = addressMapper.convertToAddress(addressDTO);
        address.setClient(client.get());
        client.get().getAddressList().add(address);
        if (client.get().getClientStatus().equals(NEW))
            client.get().setClientStatus(HAS_NOT_ORDER_YET);
        repository.save(client.get());
        return new ProjectResponse("200", "ADDED NEW ADDRESS SUCCESSFULLY");
    }

    private Long paymentPriceCalculator(Long orderId) {
        Optional<Order> order = orderService.findById(orderId);
        OrderStatus orderStatus = order.get().getOrderStatus();
        if (!orderStatus.equals(DONE)) {
            if (orderStatus.equals(PAID))
                throw new OrderStatusException(
                        "the cost of this order has already been \"PAID\"!");
            else
                throw new OrderStatusException(
                        "This order has not yet reached the payment stage, this order is in the " +
                        orderStatus + " stage!");
        }
        Optional<Offer> offer = order.get().getOfferList().stream().
                filter(o -> o.getOfferStatus().equals(ACCEPTED)).findFirst();
        return offer.get().getProposedPrice();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponseDTO> findOfferListByOrderIdBasedOnProposedPrice(Long orderId, Users users) {
        validation.checkPositiveNumber(orderId);
        validation.checkOwnerOfTheOrder(orderId, (Client) users);
        List<OfferResponseDTO> orDTOS = new ArrayList<>();
        List<Offer> list = offerService.findOfferListByOrderIdBasedOnProposedPrice(orderId);
        if (list.isEmpty())
            return orDTOS;
        list.forEach(
                o -> orDTOS.add(offerMapper.convertToDTO(o)));
        return orDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponseDTO> findOfferListByOrderIdBasedOnWorkerScore(Long orderId, Users users) {
        validation.checkPositiveNumber(orderId);
        validation.checkOwnerOfTheOrder(orderId, (Client) users);
        List<OfferResponseDTO> orDTOS = new ArrayList<>();
        List<Offer> list = offerService.findOfferListByOrderIdBasedOnWorkerScore(orderId);
        if (list.isEmpty())
            return orDTOS;
        list.forEach(
                o -> orDTOS.add(offerMapper.convertToDTO(o)));
        return orDTOS;
    }

    @Override
    public ProjectResponse chooseWorkerForOrder(Long offerId, Users users) {
        validation.checkPositiveNumber(offerId);
        validation.checkOfferBelongToTheOrder(offerId, (Client) users);
        Optional<Offer> offer = offerService.findById(offerId);
        if (offer.get().getOfferStatus().equals(ACCEPTED))
            throw new OfferStatusException(" this offer alrady accepted");
        else if (offer.get().getOfferStatus().equals(REJECTED))
            throw new OfferStatusException(" this offer alrady rejected");
        else
            orderService.chooseOffer(offer.get().getOrder(), offerId);
        return new ProjectResponse("200", "CHOOSE SUCCESSFULLY");
    }


    @Override
    public ProjectResponse changeOrderStatusToStarted(Long orderId, Users users) {
        validation.checkPositiveNumber(orderId);
        validation.checkOwnerOfTheOrder(orderId, (Client) users);
        Optional<Order> order = orderService.findById(orderId);
        if (!order.get().getOrderStatus().equals(WAITING_FOR_WORKER_TO_COME))
            throw new OrderIsNotExistException
                    ("the status of this order is not yet \"WAITING FOR EXPERT TO COME\"!");
        order.get().getOfferList().forEach(o -> {
            if (o.getOfferStatus().equals(ACCEPTED))
                if (!o.getExecutionTime().isBefore(LocalDateTime.now()))
                    throw new TimeException("the worker has not arrived at your place yet!");
        });
        order.get().setOrderStatus(STARTED);
        orderService.save(order.get());
        return new ProjectResponse("200", "ORDER STATUS CHANGED SUCCESSFULLY");
    }

    @Override
    public ProjectResponse changeOrderStatusToDone(Long orderId, Users users) {
        validation.checkPositiveNumber(orderId);
        validation.checkOwnerOfTheOrder(orderId, (Client) users);
        Optional<Order> order = orderService.findById(orderId);
        if (!order.get().getOrderStatus().equals(STARTED))
            throw new OrderIsNotExistException
                    ("the status of this order is not yet \"STARTED\"!");
        final Offer[] offer = new Offer[1];
        order.get().getOfferList().forEach(o -> {
            if (o.getOfferStatus().equals(ACCEPTED))
                offer[0] = o;
        });
        order.get().setOrderStatus(DONE);
        orderService.save(order.get());
        int hourlyDelay = (LocalDateTime.now().getHour()) - (offer[0].getEndTime().getHour());
        if (hourlyDelay > 0) {
            Worker worker = offer[0].getWorker();
            worker.delay(hourlyDelay);
            workerService.save(worker);
            String delayFormat = " HOUR";
            if (hourlyDelay > 1)
                delayFormat = " HOURS";
            return new ProjectResponse("200", "ORDER STATUS CHANGED SUCCESSFULLY WITH " +
                                              hourlyDelay + delayFormat + "DELAY");
        }
        return new ProjectResponse("200", "ORDER STATUS CHANGED SUCCESSFULLY");
    }

    @Override
    public ProjectResponse registerComment(CommentRequestDTO dto, Users users) {
        validation.checkPositiveNumber(dto.getOrderId());
        validation.checkOwnerOfTheOrder(dto.getOrderId(), (Client) users);
        validation.checkScore(dto.getScore());
        if (dto.getComment().isBlank())
            dto.setComment(PRE_WRITTEN_MESSAGE);
        else
            validation.checkText(dto.getComment());
        Optional<Order> order = orderService.findById(dto.getOrderId());
        if (!order.get().getOrderStatus().equals(DONE))
            throw new OrderIsNotExistException
                    ("the status of this order is not yet \"DONE\"!");
        Worker worker = order.get().getOfferList().stream().filter(o ->
                o.getOfferStatus().equals(ACCEPTED)).findFirst().get().getWorker();
        worker.rate(dto.getScore());
        Comment comment = commentMapper.convertToComment(dto);
        comment.setOrder(order.get());
        commentService.save(comment);
        order.get().setComment(comment);
        orderService.save(order.get());
        return new ProjectResponse("200",
                "THE COMMENT ABOUT THE ORDER WAS SUCCESSFULLY REGISTERED.");
    }

    @Override
    public List<FilterUserResponseDTO> clientFilter(FilterUserDTO clientDTO) {
//        clientDTO.setUserType(null);
        List<FilterUserResponseDTO> fcDTOS = new ArrayList<>();
        if (clientDTO.equals(null))
            return fcDTOS;
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> clientCriteriaQuery = criteriaBuilder.createQuery(Client.class);
        Root<Client> clientRoot = clientCriteriaQuery.from(Client.class);

        createFilters(clientDTO, predicateList, criteriaBuilder, clientRoot);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        clientCriteriaQuery.select(clientRoot).where(predicates);
        List<Client> resultList = entityManager.createQuery(clientCriteriaQuery).getResultList();
        if (resultList.isEmpty())
            return fcDTOS;
        resultList.forEach(rl -> fcDTOS.add(clientMapper.convertToFilterDTO(rl)));
        return fcDTOS;
    }

    private void createFilters(FilterUserDTO dto, List<Predicate> predicateList,
                               CriteriaBuilder criteriaBuilder, Root<Client> clientRoot) {
        if (dto.getFirstname() != null) {
            String firstname = "%" + dto.getFirstname() + "%";
            predicateList.add(criteriaBuilder.like(clientRoot.get("firstname"), firstname));
        }
        if (dto.getLastname() != null) {
            String lastname = "%" + dto.getLastname() + "%";
            predicateList.add(criteriaBuilder.like(clientRoot.get("lastname"), lastname));
        }
        if (dto.getUsername() != null) {
            String email = "%" + dto.getUsername() + "%";
            predicateList.add(criteriaBuilder.like(clientRoot.get("email"), email));
        }
        if (dto.getIsActive() != null)
            if (dto.getIsActive())
                predicateList.add(criteriaBuilder.isTrue(clientRoot.get("isActive")));
            else
                predicateList.add(criteriaBuilder.isFalse(clientRoot.get("isActive")));

        if (dto.getUserStatus() != null)
            predicateList.add(criteriaBuilder.equal(clientRoot.get("clientStatus"),
                    dto.getUserStatus()));

        if (dto.getMinCredit() == null && dto.getMaxCredit() != null)
            dto.setMinCredit(0L);
        if (dto.getMinCredit() != null && dto.getMaxCredit() == null)
            dto.setMaxCredit(Long.MAX_VALUE);
        if (dto.getMinCredit() != null && dto.getMaxCredit() != null)
            predicateList.add(criteriaBuilder.between(clientRoot.get("credit"),
                    dto.getMinCredit(), dto.getMaxCredit()));

        if (dto.getMinUserCreationAt() == null && dto.getMaxUserCreationAt() != null)
            dto.setMinUserCreationAt(LocalDateTime.now().minusYears(2));
        if (dto.getMinUserCreationAt() != null && dto.getMaxUserCreationAt() == null)
            dto.setMaxUserCreationAt(LocalDateTime.now());
        if (dto.getMinUserCreationAt() != null && dto.getMaxUserCreationAt() != null)
            predicateList.add(criteriaBuilder.between(clientRoot.get("registrationTime"),
                    dto.getMinUserCreationAt(), dto.getMaxUserCreationAt()));

        if (dto.getMinNumberOfOperation() == null && dto.getMaxNumberOfOperation() != null)
            dto.setMinNumberOfOperation(0);
        if (dto.getMinNumberOfOperation() != null && dto.getMaxNumberOfOperation() == null)
            dto.setMaxNumberOfOperation(Integer.MAX_VALUE);
        if (dto.getMinNumberOfOperation() != null && dto.getMaxNumberOfOperation() != null)
            predicateList.add(criteriaBuilder.between(clientRoot.get("numberOfOperation"),
                    dto.getMinNumberOfOperation(), dto.getMaxNumberOfOperation()));

        if (dto.getMinNumberOfDoneOperation() == null && dto.getMaxNumberOfDoneOperation() != null)
            dto.setMinNumberOfDoneOperation(0);
        if (dto.getMinNumberOfDoneOperation() != null && dto.getMaxNumberOfDoneOperation() == null)
            dto.setMaxNumberOfDoneOperation(Integer.MAX_VALUE);
        if (dto.getMinNumberOfDoneOperation() != null && dto.getMaxNumberOfDoneOperation() != null)
            predicateList.add(criteriaBuilder.between(clientRoot.get("paidCounter"),
                    dto.getMinNumberOfDoneOperation(), dto.getMaxNumberOfDoneOperation()));

    }
}
