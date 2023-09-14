package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.offer.enums.OfferStatus;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.enums.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import ir.maktabsharif.homeservicephase2.exception.*;
import ir.maktabsharif.homeservicephase2.mapper.MainServiceMapper;
import ir.maktabsharif.homeservicephase2.mapper.OfferMapper;
import ir.maktabsharif.homeservicephase2.mapper.WorkerMapper;
import ir.maktabsharif.homeservicephase2.repository.WorkerRepository;
import ir.maktabsharif.homeservicephase2.security.token.entity.Token;
import ir.maktabsharif.homeservicephase2.security.token.service.TokenService;
import ir.maktabsharif.homeservicephase2.service.*;
import ir.maktabsharif.homeservicephase2.util.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class WorkerServiceImpl extends BaseServiceImpl<Worker, Long, WorkerRepository>
        implements WorkerService {

    private final OfferService offerService;
    private final OrderService orderService;
    private final MainServiceService mainService;
    private final JobService jobService;

    private final WorkerMapper workerMapper;
    private final MainServiceMapper mainServiceMapper;
    private final OfferMapper offerMapper;

    private final Validation validation;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    public WorkerServiceImpl(WorkerRepository repository, OfferService offerService,
                             OrderService orderService, MainServiceService mainService,
                             JobService jobService, WorkerMapper workerMapper,
                             MainServiceMapper mainServiceMapper, OfferMapper offerMapper,
                             Validation validation, TokenService tokenService,
                             EmailService emailService, PasswordEncoder passwordEncoder,
                             EntityManager entityManager) {
        super(repository);
        this.offerService = offerService;
        this.orderService = orderService;
        this.mainService = mainService;
        this.jobService = jobService;
        this.workerMapper = workerMapper;
        this.mainServiceMapper = mainServiceMapper;
        this.offerMapper = offerMapper;
        this.validation = validation;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    @Override
    public String addNewWorker(WorkerRegistrationDTO workerRegistrationDTO) throws IOException {
        validation.checkEmail(workerRegistrationDTO.getEmail());
        if (repository.findByEmail(workerRegistrationDTO.getEmail()).isPresent())
            throw new DuplicateEmailException("this Email already exist!");
        validation.checkPassword(workerRegistrationDTO.getPassword());
        validation.checkText(workerRegistrationDTO.getFirstname());
        validation.checkText(workerRegistrationDTO.getLastname());
        validation.checkImage(workerRegistrationDTO.getFile());
        Worker worker = workerMapper.convertToNewWorker(workerRegistrationDTO);
        repository.save(worker);
        String newToken = UUID.randomUUID().toString();
        Token token = new Token(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), worker);
        token.setToken(newToken);
        tokenService.saveToken(token);
        SimpleMailMessage mailMessage =
                emailService.createEmail(worker.getEmail(), worker.getFirstname(), token.getToken(), worker.getRole());
        emailService.sendEmail(mailMessage);
        return newToken;
    }


    @Override
    @Transactional
    public ProjectResponse editPassword(ChangePasswordDTO changePasswordDTO, Long workerId) {
        validation.checkPassword(changePasswordDTO.getNewPassword());
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword()))
            throw new DuplicatePasswordException("this confirmNewPassword not match with newPassword!");
//        Optional<Worker> worker = repository.findById(workerId);
//        worker.get().setPassword(passwordEncoder.encode(changePasswordDTO.getConfirmNewPassword()));
        repository.editPassword(workerId, passwordEncoder.encode(changePasswordDTO.getConfirmNewPassword()));
        return new ProjectResponse("200", "CHANGE PASSWORD SUCCESSFULLY");
    }

    @Override
    @Transactional(readOnly = true)
    public List<MainServiceResponseDTO> showAllMainServices() {
        List<MainService> mainServices = mainService.findAll();
        List<MainServiceResponseDTO> msDTOS = new ArrayList<>();
        if (!mainServices.isEmpty())
            mainServices.forEach(ms -> msDTOS.add(mainServiceMapper.convertToDTO(ms)));
        return msDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> showJobs(ChooseMainServiceDTO dto) {
        Optional<MainService> dbMainService = Optional.empty();
        if (!dto.getMainServiceName().isBlank()) {
            validation.checkText(dto.getMainServiceName());
            dbMainService = mainService.findByName(dto.getMainServiceName());
        } else if (!dto.getMainServiceId().toString().isBlank()) {
            validation.checkPositiveNumber(dto.getMainServiceId());
            dbMainService = mainService.findById(dto.getMainServiceId());
        }
        if (dbMainService.isEmpty())
            throw new MainServiceIsNotExistException("this main service dose not exist!");
        return jobService.findByMainServiceId(dbMainService.get().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LimitedOrderResponseDTO> showRelatedOrders(Long workerId) {
        Optional<Worker> worker = repository.findById(workerId);
        if (worker.get().getJobList().isEmpty())
            throw new WorkerNoAccessException("you do not have a job title!");
        List<LimitedOrderResponseDTO> lorDTOS = new ArrayList<>();
        worker.get().getJobList().forEach(job ->
                lorDTOS.addAll(orderService.findAllOrdersByJobNameAndProvince(
                        job.getName(), worker.get().getProvince())));
        return lorDTOS;
    }

    @Override
    public ProjectResponse submitAnOffer(OfferRequestDTO offerRequestDTO, Long workerId) {
        validation.checkPositiveNumber(offerRequestDTO.getOrderId());
        validation.checkPositiveNumber(offerRequestDTO.getOfferProposedPrice());
        Optional<Worker> dbWorker = repository.findById(workerId);
        if (!(dbWorker.get().getIsActive()))
            throw new WorkerNoAccessException("this dbWorker is inActive");
        if (!(dbWorker.get().getStatus().equals(WorkerStatus.CONFIRMED)))
            throw new WorkerNoAccessException("the status of dbWorker is not CONFIRMED");
        Optional<Order> order = orderService.findById(offerRequestDTO.getOrderId());
        if (order.isEmpty())
            throw new OrderIsNotExistException("this order does not exist!");
        OrderStatus orderStatus = order.get().getOrderStatus();
        if (!(orderStatus.equals(OrderStatus.WAITING_FOR_WORKER_SUGGESTION) ||
              orderStatus.equals(OrderStatus.WAITING_FOR_WORKER_SELECTION)))
            throw new OrderStatusException("!this order has already accepted the offer");
        if (!(dbWorker.get().getJobList().contains(order.get().getJob())))
            throw new WorkerNoAccessException("this dbWorker does not have such job title!");
        if (offerRequestDTO.getProposedEndDate().isBefore(offerRequestDTO.getProposedStartDate()))
            throw new TimeException("time does not go back!");
        if (offerRequestDTO.getProposedStartDate().isBefore(order.get().getExecutionTime()))
            throw new TimeException("no order has been in your proposed time for begin job!");
        if (order.get().getProposedPrice() > offerRequestDTO.getOfferProposedPrice())
            throw new AmountLessExseption("the proposed-price should not be lower than the order proposed-price!");
        Offer offer = offerMapper.convertToNewOffer(offerRequestDTO);
        offer.setWorker(dbWorker.get());
        offer.setOrder(order.get());
        offerService.save(offer);
        if (orderStatus.equals(OrderStatus.WAITING_FOR_WORKER_SUGGESTION))
            order.get().setOrderStatus(OrderStatus.WAITING_FOR_WORKER_SELECTION);
        dbWorker.get().setNumberOfOperation(dbWorker.get().getNumberOfOperation() + 1);
        orderService.save(order.get());
        return new ProjectResponse("200", "ADDED OFFER SUCCESSFUL");
    }

    @Override
    @Transactional(readOnly = true)
    public double getWorkerRate(Long workerId) {
        validation.checkPositiveNumber(workerId);
        Optional<Worker> worker = repository.findById(workerId);
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        return worker.get().getScore();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getWorkerCredit(Long workerId) {
        validation.checkPositiveNumber(workerId);
        Optional<Worker> worker = repository.findById(workerId);
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        return worker.get().getCredit();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponseDTO> showAllOffersWaiting(Long workerId) {
        List<Offer> offers = offerService.findOffersByWorkerIdAndOfferStatus(workerId, OfferStatus.WAITING);
        List<OfferResponseDTO> orDTOS = new ArrayList<>();
        offers.forEach(o -> orDTOS.add(offerMapper.convertToDTO(o)));
        return orDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponseDTO> showAllOffersAccepted(Long workerId) {
        List<Offer> offers = offerService.findOffersByWorkerIdAndOfferStatus(workerId, OfferStatus.ACCEPTED);
        List<OfferResponseDTO> orDTOS = new ArrayList<>();
        offers.forEach(o -> orDTOS.add(offerMapper.convertToDTO(o)));
        return orDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponseDTO> showAllOffersRejected(Long workerId) {
        List<Offer> offers = offerService.findOffersByWorkerIdAndOfferStatus(workerId, OfferStatus.REJECTED);
        List<OfferResponseDTO> orDTOS = new ArrayList<>();
        offers.forEach(o -> orDTOS.add(offerMapper.convertToDTO(o)));
        return orDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Worker> findAll() {
        List<Worker> workerList = repository.findAll();
        if (workerList.isEmpty())
            throw new WorkerIsNotExistException("there are no workers!");
        return workerList;
    }

    @Override
    public Optional<Worker> findByUsername(String email) {
        return Optional.empty();
    }

    @Override
    public List<FilterUserResponseDTO> allWorker(FilterUserDTO userDTO) {
        List<FilterUserResponseDTO> furDList = new ArrayList<>();
        List<Worker> workerList = repository.findAll();
        if (!workerList.isEmpty())
            workerList.forEach(c ->
                    furDList.add(workerMapper.convertToFilterDTO(c)));
        return furDList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilterUserResponseDTO> workerFilter(FilterUserDTO workerDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Worker> workerCriteriaQuery = criteriaBuilder.createQuery(Worker.class);
        Root<Worker> workerRoot = workerCriteriaQuery.from(Worker.class);
        createFilters(workerDTO, predicateList, criteriaBuilder, workerRoot);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        workerCriteriaQuery.select(workerRoot).where(predicates);
        List<Worker> resultList = entityManager.createQuery(workerCriteriaQuery).getResultList();
        List<FilterUserResponseDTO> fuDTOS = new ArrayList<>();
        resultList.forEach(rl -> fuDTOS.add(workerMapper.convertToFilterDTO(rl)));
        return fuDTOS;
    }

    private void createFilters(FilterUserDTO workerDTO, List<Predicate> predicateList,
                               CriteriaBuilder criteriaBuilder, Root<Worker> workerRoot) {

        if (workerDTO.getFirstname() != null) {
            String firstname = "%" + workerDTO.getFirstname() + "%";
            predicateList.add(criteriaBuilder.like(workerRoot.get("firstname"), firstname));
        }
        if (workerDTO.getLastname() != null) {
            String lastname = "%" + workerDTO.getLastname() + "%";
            predicateList.add(criteriaBuilder.like(workerRoot.get("lastname"), lastname));
        }
        if (workerDTO.getUsername() != null) {
            String email = "%" + workerDTO.getUsername() + "%";
            predicateList.add(criteriaBuilder.like(workerRoot.get("email"), email));
        }

        if (workerDTO.getIsActive() != null)
            if (workerDTO.getIsActive())
                predicateList.add(criteriaBuilder.isTrue(workerRoot.get("isActive")));
            else
                predicateList.add(criteriaBuilder.isFalse(workerRoot.get("isActive")));

        if (workerDTO.getUserStatus() != null)
            predicateList.add(criteriaBuilder.equal(workerRoot.get("status"),
                    workerDTO.getUserStatus().toString()));

        if (workerDTO.getMinCredit() == null && workerDTO.getMaxCredit() != null)
            workerDTO.setMinCredit(0L);
        if (workerDTO.getMinCredit() != null && workerDTO.getMaxCredit() == null)
            workerDTO.setMaxCredit(Long.MAX_VALUE);
        if (workerDTO.getMinCredit() != null && workerDTO.getMaxCredit() != null)
            predicateList.add(criteriaBuilder.between(workerRoot.get("credit"),
                    workerDTO.getMinCredit(), workerDTO.getMaxCredit()));

        if (workerDTO.getMinScore() == null && workerDTO.getMaxScore() != null)
            workerDTO.setMinScore(0.0);
        if (workerDTO.getMinScore() != null && workerDTO.getMaxScore() == null)
            workerDTO.setMaxScore(5.0);
        if (workerDTO.getMinScore() != null && workerDTO.getMaxScore() != null)
            predicateList.add(criteriaBuilder.between(workerRoot.get("score"),
                    workerDTO.getMinScore(), workerDTO.getMaxScore()));

        if (workerDTO.getMinUserCreationAt() == null && workerDTO.getMaxUserCreationAt() != null)
            workerDTO.setMinUserCreationAt(LocalDateTime.now().minusYears(2));
        if (workerDTO.getMinUserCreationAt() != null && workerDTO.getMaxUserCreationAt() == null)
            workerDTO.setMaxUserCreationAt(LocalDateTime.now());
        if (workerDTO.getMinUserCreationAt() != null && workerDTO.getMaxUserCreationAt() != null)
            predicateList.add(criteriaBuilder.between(workerRoot.get("registrationTime"),
                    workerDTO.getMinUserCreationAt(), workerDTO.getMaxUserCreationAt()));

        if (workerDTO.getMinNumberOfOperation() == null && workerDTO.getMaxNumberOfOperation() != null)
            workerDTO.setMinNumberOfOperation(0);
        if (workerDTO.getMinNumberOfOperation() != null && workerDTO.getMaxNumberOfOperation() == null)
            workerDTO.setMaxNumberOfOperation(Integer.MAX_VALUE);
        if (workerDTO.getMinNumberOfOperation() != null && workerDTO.getMaxNumberOfOperation() != null)
            predicateList.add(criteriaBuilder.between(workerRoot.get("numberOfOperation"),
                    workerDTO.getMinNumberOfOperation(), workerDTO.getMaxNumberOfOperation()));

        if (workerDTO.getMinNumberOfDoneOperation() == null && workerDTO.getMaxNumberOfDoneOperation() != null)
            workerDTO.setMinNumberOfDoneOperation(0);
        if (workerDTO.getMinNumberOfDoneOperation() != null && workerDTO.getMaxNumberOfDoneOperation() == null)
            workerDTO.setMaxNumberOfDoneOperation(Integer.MAX_VALUE);
        if (workerDTO.getMinNumberOfDoneOperation() != null && workerDTO.getMaxNumberOfDoneOperation() != null)
            predicateList.add(criteriaBuilder.between(workerRoot.get("rateCounter"),
                    workerDTO.getMinNumberOfDoneOperation(), workerDTO.getMaxNumberOfDoneOperation()));

    }

}
