package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.dto.request.ManagerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.entity.user.Admin;
import ir.maktabsharif.homeservicephase2.exception.DuplicateEmailException;
import ir.maktabsharif.homeservicephase2.mapper.AdminMapper;
import ir.maktabsharif.homeservicephase2.repository.AdminRepository;
import ir.maktabsharif.homeservicephase2.security.token.entity.Token;
import ir.maktabsharif.homeservicephase2.security.token.service.TokenService;
import ir.maktabsharif.homeservicephase2.service.EmailService;
import ir.maktabsharif.homeservicephase2.service.ManagerService;
import ir.maktabsharif.homeservicephase2.util.Validation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ManagerServiceImpl extends BaseServiceImpl<Admin, Long, AdminRepository>
        implements ManagerService {

    private final Validation validation;
    private final AdminMapper adminMapper;
    private final TokenService tokenService;
    private final EmailService emailService;

    public ManagerServiceImpl(AdminRepository repository, Validation validation,
                              AdminMapper adminMapper, TokenService tokenService,
                              EmailService emailService) {
        super(repository);
        this.validation = validation;
        this.adminMapper = adminMapper;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Override
    public Admin findManager() {
        return repository.findAll().stream().filter(a
                ->a.isManager()).findFirst().get();

    }

    @Override
    public String addNewManager(ManagerRegistrationDTO dto) {
        validation.checkEmail(dto.getEmail());
        if (repository.findByEmail(dto.getEmail()).isPresent())
            throw new DuplicateEmailException("this Email already exist!");
        Admin manager = adminMapper.convertToNewManager(dto);
        repository.save(manager);
        String newToken = UUID.randomUUID().toString();
        Token token = new Token(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), manager);
        token.setToken(newToken);
        tokenService.saveToken(token);
        SimpleMailMessage mailMessage =
                emailService.createEmail(manager.getEmail(), manager.getFirstname(),
                        token.getToken(), manager.getRole());
        emailService.sendEmail(mailMessage);
        return newToken;
    }

//    private final MainServiceService mainServiceService;
//    private final JobService jobService;
//    private final WorkerService workerService;
//    private final ClientService clientService;
//
//    private final MainServiceMapper mainServiceMapper;
//    private final JobMapper jobMapper;
//    private final WorkerMapper workerMapper;
//    private final AdminMapper adminMapper;
//
//    private final Validation validation;
//    private final TokenService tokenService;
//    private final EmailService emailService;

    //    public ManagerServiceImpl(AdminRepository repository, MainServiceService mainServiceService,
//                              JobService jobService, WorkerService workerService,
//                              ClientService clientService, MainServiceMapper mainServiceMapper,
//                              JobMapper jobMapper, WorkerMapper workerMapper,
//                              AdminMapper adminMapper, Validation validation,
//                              TokenService tokenService, EmailService emailService) {
//        super(repository);
//        this.mainServiceService = mainServiceService;
//        this.jobService = jobService;
//        this.workerService = workerService;
//        this.clientService = clientService;
//        this.mainServiceMapper = mainServiceMapper;
//        this.jobMapper = jobMapper;
//        this.workerMapper = workerMapper;
//        this.adminMapper = adminMapper;
//        this.validation = validation;
//        this.tokenService = tokenService;
//        this.emailService = emailService;
//    }

//
//    @Override
//    public String addNewAdmin(AdminRegistrationDTO dto) {
//        validation.checkEmail(dto.getEmail());
//        if (repository.findByEmail(dto.getEmail()).isPresent())
//            throw new DuplicateEmailException("this Email already exist!");
//        Admin admin = adminMapper.convertToNewAdmin(dto);
//        repository.save(admin);
//        String newToken = UUID.randomUUID().toString();
//        Token token = new Token(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), admin);
//        token.setToken(newToken);
//        tokenService.saveToken(token);
//        SimpleMailMessage mailMessage =
//                emailService.createEmail(admin.getEmail(), admin.getFirstname(), token.getToken(), admin.getRole());
//        emailService.sendEmail(mailMessage);
//        return newToken;
//    }
//
//    @Override
//    public ProjectResponse createMainService(MainServiceRequestDTO msDTO) {
//        validation.checkText(msDTO.getName());
//        if (mainServiceService.findByName(msDTO.getName()).isPresent())
//            throw new MainServiceIsExistException("this main service already exist!");
//        MainService newMainService = new MainService(msDTO.getName());
//        mainServiceService.save(newMainService);
//        return new ProjectResponse("200", "ADDED SUCCESSFUL");
//    }
//
//    @Override
//    public ProjectResponse deleteMainService(String name) {
//        validation.checkText(name);
//        if (mainServiceService.findByName(name).isEmpty())
//            throw new MainServiceIsNotExistException("this main service dose not exist!");
//        mainServiceService.deleteMainServiceByName(name);
//        return new ProjectResponse("200", "DELETED SUCCESSFUL");
//    }
//
//    @Override
//    public ProjectResponse addJob(JobRequestDTO jobRequestDTO) {
//        String mainServiceName = jobRequestDTO.getMainServiceRequest();
//        String jobName = jobRequestDTO.getName();
//        validation.checkText(mainServiceName);
//        validation.checkText(jobName);
//        validation.checkPositiveNumber(jobRequestDTO.getBasePrice());
//        validation.checkBlank(jobRequestDTO.getDescription());
//        Optional<MainService> mainService = mainServiceService.findByName(mainServiceName);
//        if (mainService.isEmpty())
//            throw new MainServiceIsNotExistException("this main service dose not exist!");
//        if (jobService.findByName(jobName).isPresent())
//            throw new JobIsExistException("this job already exist!");
//        Job newJob = jobMapper.convertToJob(jobRequestDTO);
//        newJob.setMainService(mainService.get());
//        jobService.save(newJob);
//        return new ProjectResponse("200", "ADDED SUCCESSFUL");
//    }
//
//    @Override
//    public ProjectResponse deleteJob(String name) {
//        validation.checkText(name);
//        if (jobService.findByName(name).isEmpty())
//            throw new JobIsNotExistException("this job dose not exist!");
//        jobService.deleteJobByName(name);
//        return new ProjectResponse("200", "DELETED SUCCESSFUL");
//    }
//
//    @Override
//    public ProjectResponse addWorkerToJob(Long jobId, Long workerId) {
//        validation.checkPositiveNumber(jobId);
//        validation.checkPositiveNumber(workerId);
//        Optional<Job> job = jobService.findById(jobId);
//        if (job.isEmpty())
//            throw new JobIsNotExistException("this job dose not exist!");
//        Optional<Worker> worker = workerService.findById(workerId);
//        if (worker.isEmpty())
//            throw new WorkerIsNotExistException("this worker does not exist!");
//        if (!(worker.get().getStatus().equals(WorkerStatus.CONFIRMED)))
//            throw new WorkerNoAccessException("the status of expert is not CONFIRMED");
//        worker.get().addJob(job.get());
//        workerService.save(worker.get());
//        return new ProjectResponse("200", "ADDED SUCCESSFUL");
//    }
//
//    @Override
//    public ProjectResponse deleteJobFromWorker(Long jobId, Long workerId) {
//        validation.checkPositiveNumber(jobId);
//        validation.checkPositiveNumber(workerId);
//        Optional<Job> job = jobService.findById(jobId);
//        if (job.isEmpty())
//            throw new JobIsNotExistException("this job dose not exist!");
//        Optional<Worker> worker = workerService.findById(workerId);
//        if (worker.isEmpty())
//            throw new WorkerIsNotExistException("this worker does not exist!");
//        worker.get().deleteJob(job.get());
//        workerService.save(worker.get());
//        return new ProjectResponse("200", "DELETED SUCCESSFUL");
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<MainServiceResponseDTO> findAllMainService() {
//        List<MainService> mainServices = mainServiceService.findAll();
//        List<MainServiceResponseDTO> msDTOS = new ArrayList<>();
//        mainServices.forEach(ms -> msDTOS.add(mainServiceMapper.convertToDTO(ms)));
//        return msDTOS;
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<JobResponseDTO> findAllJob() {
//        List<Job> jobs = jobService.findAll();
//        List<JobResponseDTO> jDTOS = new ArrayList<>();
//        jobs.forEach(j -> jDTOS.add(jobMapper.convertToDTO(j)));
//        return jDTOS;
//    }
//
//    @Override
//    public ProjectResponse editJobCustom(UpdateJobDTO updateJobDTO) {
//        validation.checkText(updateJobDTO.getName());
//        Optional<Job> job;
//        if (updateJobDTO.getJobId() != null) {
//            validation.checkPositiveNumber(updateJobDTO.getJobId());
//            job = jobService.findById(updateJobDTO.getJobId());
//            if (job.isEmpty())
//                throw new JobIsNotExistException("this job dose not exist!");
//            job.get().setName(updateJobDTO.getName());
//        } else {
//            job = jobService.findByName(updateJobDTO.getName());
//            if (job.isEmpty())
//                throw new JobIsNotExistException("this job dose not exist!");
//        }
//        if (updateJobDTO.getDescription().isEmpty() &&
//            updateJobDTO.getBasePrice() == 0L) {
//            throw new JobIsNotExistException("change titles are empty!");
//        } else if (!updateJobDTO.getDescription().isEmpty()) {
//            validation.checkText(updateJobDTO.getDescription());
//            job.get().setDescription(updateJobDTO.getDescription());
//        }
//        if (updateJobDTO.getBasePrice() != 0L) {
//            validation.checkPositiveNumber(updateJobDTO.getBasePrice());
//            job.get().setBasePrice(updateJobDTO.getBasePrice());
//        }
//        jobService.save(job.get());
//        return new ProjectResponse("200", "UPDATED SUCCESSFUL");
//
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<WorkerResponseDTO> findAllWorkers() {
//        List<Worker> workers = workerService.findAll();
//        List<WorkerResponseDTO> wDTOS = new ArrayList<>();
//        workers.forEach(w -> wDTOS.add(workerMapper.convertToDTO(w)));
//        return wDTOS;
//    }
//
//    @Override
//    public ProjectResponse confirmWorker(Long workerId) {
//        validation.checkPositiveNumber(workerId);
//        Optional<Worker> worker = workerService.findById(workerId);
//        if (worker.isEmpty())
//            throw new WorkerIsNotExistException("this worker does not exist!");
//        if (worker.get().getIsActive()) {
//            if (worker.get().getStatus().equals(WorkerStatus.CONFIRMED))
//                throw new WorkerIsHoldsExistException("this worker is currently certified!");
//            worker.get().setStatus(WorkerStatus.CONFIRMED);
//        } else {
//            worker.get().setIsActive(true);
//        }
//        workerService.save(worker.get());
//        return new ProjectResponse("200", "UPDATED SUCCESSFUL");
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<JobResponseDTO> findAllJobsByMainService(Long mainServiceId) {
//        validation.checkPositiveNumber(mainServiceId);
//        if (mainServiceService.findById(mainServiceId).isEmpty())
//            throw new MainServiceIsNotExistException("this main service dose not exist!");
//        return jobService.findByMainServiceId(mainServiceId);
//    }
//
//    @Override
//    public ProjectResponse deActiveWorker(Long workerId) {
//        validation.checkPositiveNumber(workerId);
//        Optional<Worker> worker = workerService.findById(workerId);
//        if (worker.isEmpty())
//            throw new WorkerIsNotExistException("this worker does not exist!");
//        if (!worker.get().getIsActive())
//            throw new WorkerIsHoldsExistException("this worker is currently deActive");
//        worker.get().setIsActive(false);
//        workerService.save(worker.get());
//        return new ProjectResponse("200", "UPDATED SUCCESSFUL");
//    }
//
//    @Override
//    public List<FilterWorkerResponseDTO> workerFilter(FilterWorkerDTO workerDTO) {
//        return workerService.workerFilter(workerDTO);
//    }
//
//    @Override
//    public List<FilterClientResponseDTO> clientFilter(FilterClientDTO clientDTO) {
//        return clientService.clientFilter(clientDTO);
//    }

    @Override
    public Optional<Admin> findByUsername(String email) {
        return repository.findByEmail(email);
    }


//    @Override
//    public List<FilterWorkerResponseDTO> userFilter(FilterWorkerDTO workerDTO) {
//        return null;
//    }
//
//    @Override
//    public List<FilterWorkerResponseDTO> userFilter(FilterWorkerDTO userDTO) {
//        List<FilterWorkerResponseDTO> furd
//        if (userDTO.getUserType().equals("worker"))
//            return workerService.workerFilter(userDTO);
//        if (userDTO.getUserType().equals("client"))
//            return workerService.workerFilter(userDTO);
//    }
}
