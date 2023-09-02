package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.offer.OfferStatus;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import ir.maktabsharif.homeservicephase2.entity.order.OrderStatus;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import ir.maktabsharif.homeservicephase2.exception.*;
import ir.maktabsharif.homeservicephase2.mapper.MainServiceMapper;
import ir.maktabsharif.homeservicephase2.mapper.OfferMapper;
import ir.maktabsharif.homeservicephase2.mapper.WorkerMapper;
import ir.maktabsharif.homeservicephase2.repository.WorkerRepository;
import ir.maktabsharif.homeservicephase2.service.*;
import ir.maktabsharif.homeservicephase2.util.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final MainServiceService mainService;
    private final JobService jobService;

    private final WorkerMapper workerMapper;
    private final MainServiceMapper mainServiceMapper;
    private final OfferMapper offerMapper;

    private final Validation validation;

    @PersistenceContext
    private EntityManager entityManager;

    public WorkerServiceImpl(WorkerRepository repository, OfferService offerService,
                             OrderService orderService, MainServiceService mainService,
                             JobService jobService, WorkerMapper workerMapper,
                             MainServiceMapper mainServiceMapper, OfferMapper offerMapper,
                             Validation validation, EntityManager entityManager) {
        super(repository);
        this.offerService = offerService;
        this.orderService = orderService;
        this.mainService = mainService;
        this.jobService = jobService;
        this.workerMapper = workerMapper;
        this.mainServiceMapper = mainServiceMapper;
        this.offerMapper = offerMapper;
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
    public ProjectResponse addWorker(UserRegistrationDTO workerRegistrationDTO) {
        validation.checkEmail(workerRegistrationDTO.getEmail());
        if (repository.findByEmail(workerRegistrationDTO.getEmail()).isPresent())
            throw new DuplicateEmailException("this Email already exist!");
        validation.checkText(workerRegistrationDTO.getFirstname());
        validation.checkText(workerRegistrationDTO.getLastname());
        validation.checkPassword(workerRegistrationDTO.getPassword());
        MultipartFile image = workerRegistrationDTO.getFile();
        validation.checkImage(image);
        Worker worker = workerMapper.convertToWorker(workerRegistrationDTO);
        try {
            worker.setImage(workerRegistrationDTO.getFile().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        worker.setStatus(WorkerStatus.AWAITING);
        worker.setCredit(0L);
        worker.setIsActive(false);
        repository.save(worker);
        return new ProjectResponse("200", "ADDED SUCCESSFUL");
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse loginWorker(LoginDTO workerLoginDto) {
        validation.checkEmail(workerLoginDto.getUsername());
        Optional<Worker> worker = repository.findByEmail(workerLoginDto.getUsername());
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        if (!worker.get().getPassword().equals(workerLoginDto.getPassword()))
            throw new PasswordIncorrect("this password incorrect!");
        return new ProjectResponse("200", "LOGIN SUCCESSFUL");
    }

    @Override
    public ProjectResponse editPassword(ChangePasswordDTO changePasswordDTO) {
        LoginDTO loginDTO = new LoginDTO(changePasswordDTO.getUsername(), changePasswordDTO.getPassword());
        loginWorker(loginDTO);
        validation.checkPassword(changePasswordDTO.getNewPassword());
        if (changePasswordDTO.getPassword().equals(changePasswordDTO.getNewPassword()))
            throw new DuplicatePasswordException("this new password has duplicate!");
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword()))
            throw new DuplicatePasswordException("this confirm new password not match with new password!");
        Optional<Worker> worker = repository.findByEmail(changePasswordDTO.getUsername());
        worker.get().setPassword(changePasswordDTO.getConfirmNewPassword());
        return new ProjectResponse("200", "CHANGED PASSWORD SUCCESSFUL");
    }

    @Override
    @Transactional(readOnly = true)
    public List<MainServiceResponseDTO> showAllMainServices() {
        List<MainService> mainServices = mainService.findAll();
        List<MainServiceResponseDTO> msDTOS = new ArrayList<>();
        mainServices.forEach(ms -> msDTOS.add(mainServiceMapper.convertToDTO(ms)));
        return msDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDTO> showJobs(Long mainServiceId) {
        validation.checkPositiveNumber(mainServiceId);
        if (mainService.findById(mainServiceId).isEmpty())
            throw new MainServiceIsNotExistException("this main service dose not exist!");
        return jobService.findByMainServiceId(mainServiceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> showRelatedOrders(Long workerId) {
        validation.checkPositiveNumber(workerId);
        Optional<Worker> worker = repository.findById(workerId);
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        List<OrderResponseDTO> orDTOS = new ArrayList<>();
        worker.get().getJobSet().forEach(job -> orDTOS.addAll(orderService.findAllOrdersByJobName(job.getName())));
        return orDTOS;
    }

    @Override
    public ProjectResponse submitAnOffer(OfferRequestDTO offerRequestDTO) {
        validation.checkPositiveNumber(offerRequestDTO.getWorkerId());
        validation.checkPositiveNumber(offerRequestDTO.getOrderId());
        validation.checkPositiveNumber(offerRequestDTO.getOfferPrice());
        Optional<Worker> worker = repository.findById(offerRequestDTO.getWorkerId());
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        if (!(worker.get().getIsActive()))
            throw new WorkerNoAccessException("this worker is inActive");
        if (!(worker.get().getStatus().equals(WorkerStatus.CONFIRMED)))
            throw new WorkerNoAccessException("the status of worker is not CONFIRMED");
        Optional<Order> order = orderService.findById(offerRequestDTO.getOrderId());
        if (order.isEmpty())
            throw new OrderIsNotExistException("this order does not exist!");
        if (!(worker.get().getJobSet().contains(order.get().getJob())))
            throw new WorkerNoAccessException("this worker does not have such job!");
        if (offerRequestDTO.getProposedEndDate().isBefore(offerRequestDTO.getProposedStartDate()))
            throw new TimeException("time does not go back!");
        if (offerRequestDTO.getProposedStartDate().isBefore(order.get().getExecutionTime()))
            throw new TimeException("no order has been in your proposed time for begin job!");
        if (order.get().getJob().getBasePrice() > offerRequestDTO.getOfferPrice())
            throw new AmountLessExseption("the proposed-price should not be lower than the base-price!");
        OrderStatus orderStatus = order.get().getOrderStatus();
        if (!(orderStatus.equals(OrderStatus.WAITING_FOR_WORKER_SUGGESTION) ||
              orderStatus.equals(OrderStatus.WAITING_FOR_WORKER_SELECTION)))
            throw new OrderStatusException("the status of this order not" +
                                           " \"WAITING FOR EXPERT SUGGESTION\" or" +
                                           " \"WAITING FOR EXPERT SELECTION\"!");
        Offer offer = new Offer();
        offer.setWorker(worker.get());
        offer.setOrder(order.get());
        offer.setProposedPrice(offerRequestDTO.getOfferPrice());
        offer.setOfferStatus(OfferStatus.WAITING);
        offer.setExecutionTime(offerRequestDTO.getProposedStartDate());
        offer.setDurationTime(offer.getDurationTime());
        offer.setTimeType(offerRequestDTO.getType());
        offer.setEndTime(offerRequestDTO.getProposedEndDate());
        offerService.save(offer);
        if (orderStatus.equals(OrderStatus.WAITING_FOR_WORKER_SUGGESTION))
            order.get().setOrderStatus(OrderStatus.WAITING_FOR_WORKER_SELECTION);
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
    public void changeWorkerStatus(String workerUsername, WorkerStatus workerStatus) {
        Optional<Worker> worker = repository.findByEmail(workerUsername);
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        worker.get().setStatus(workerStatus);
        repository.save(worker.get());
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
    @Transactional(readOnly = true)
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
