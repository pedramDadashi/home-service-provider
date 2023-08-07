package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.base.service.BaseServiceImpl;
import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Admin;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import ir.maktabsharif.homeservicephase2.exception.*;
import ir.maktabsharif.homeservicephase2.repository.AdminRepository;
import ir.maktabsharif.homeservicephase2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long, AdminRepository>
        implements AdminService {

    private final MainServiceService MAIN_SERVICE_SERVICE;
    private final JobService JOB_SERVICE;
    private final WorkerService WORKER_SERVICE;
    private final OfferService OFFER_SERVICE;
    private final ClientService CLIENT_SERVICE;
    private final OrderService ORDER_SERVICE;

    @Autowired
    public AdminServiceImpl(AdminRepository repository, MainServiceService MAIN_SERVICE_SERVICE,
                            JobService JOB_SERVICE, WorkerService WORKER_SERVICE,
                            OfferService OFFER_SERVICE, ClientService CLIENT_SERVICE,
                            OrderService ORDER_SERVICE) {
        super(repository);
        this.MAIN_SERVICE_SERVICE = MAIN_SERVICE_SERVICE;
        this.JOB_SERVICE = JOB_SERVICE;
        this.WORKER_SERVICE = WORKER_SERVICE;
        this.OFFER_SERVICE = OFFER_SERVICE;
        this.CLIENT_SERVICE = CLIENT_SERVICE;
        this.ORDER_SERVICE = ORDER_SERVICE;
    }

    @Override
    public void createMainService(String name) {
        if (MAIN_SERVICE_SERVICE.findByName(name).isPresent())
            throw new MainServiceIsExistException("this main service already exist!");
        MainService newMainService = new MainService(name);
        MAIN_SERVICE_SERVICE.save(newMainService);
    }

    @Override
    public void deleteMainService(String name) {
        if (MAIN_SERVICE_SERVICE.findByName(name).isEmpty())
            throw new MainServiceIsNotExistException("this main service dose not exist!");
        MAIN_SERVICE_SERVICE.deleteMainServiceByName(name);
    }

    @Override
    public void addJob(String mainServiceName, String jobName, Long basePrice, String description) {
        Optional<MainService> mainService = Optional.of(MAIN_SERVICE_SERVICE.findByName(mainServiceName).get());
        if (mainService.isEmpty())
            throw new MainServiceIsNotExistException("this main service dose not exist!");
        if (JOB_SERVICE.findByName(jobName).isPresent())
            throw new JobIsExistException("this job already exist!");
        Job newJob = new Job(jobName, basePrice, description, mainService.get());
        JOB_SERVICE.save(newJob);
    }

    @Override
    public void deleteJob(String name) {
        if (JOB_SERVICE.findByName(name).isEmpty())
            throw new JobIsNotExistException("this job dose not exist!");
        JOB_SERVICE.deleteJobByName(name);
    }

    @Override
    public void addWorkerToJob(String jobName, String workerEmail) {
        Optional<Job> job = Optional.of(JOB_SERVICE.findByName(jobName).get());
        if (job.isEmpty())
            throw new JobIsNotExistException("this job dose not exist!");
        Optional<Worker> worker = Optional.of(WORKER_SERVICE.findByUsername(workerEmail).get());
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        if (!(worker.get().getStatus().equals(WorkerStatus.CONFIRMED)))
            throw new WorkerNoAccessException("the status of expert is not CONFIRMED");
        worker.get().addJob(job.get());
        WORKER_SERVICE.save(worker.get());
    }

    @Override
    public void deleteWorkerFromJob(String jobName, String workerEmail) {
        Optional<Job> job = Optional.of(JOB_SERVICE.findByName(jobName).get());
        if (job.isEmpty())
            throw new JobIsNotExistException("this job dose not exist!");
        Optional<Worker> worker = Optional.of(WORKER_SERVICE.findByUsername(workerEmail).get());
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        worker.get().deleteJob(job.get());
        WORKER_SERVICE.save(worker.get());
    }

    @Override
    public List<MainService> findAllMainService() {
        List<MainService> mainServiceList = MAIN_SERVICE_SERVICE.findAll();
        if (mainServiceList.isEmpty())
            throw new MainServiceIsNotExistException("there are no main services!");
        return mainServiceList;
    }

    @Override
    public List<Job> findAllJob() {
        List<Job> jobList = JOB_SERVICE.findAll();
        if (jobList.isEmpty())
            throw new JobIsNotExistException("there are no jobs!");
        return jobList;
    }

    @Override
    public void editJob(String jobName, Long basePrice, String description) {
        if (JOB_SERVICE.findByName(jobName).isEmpty())
            throw new JobIsNotExistException("this job dose not exist!");
        JOB_SERVICE.editBasePriceAndDescription(jobName, basePrice, description);
    }

    @Override
    public List<Worker> findAllWorkers() {
        List<Worker> workerList = WORKER_SERVICE.findAll();
        if (workerList.isEmpty())
            throw new WorkerIsNotExistException("there are no workers!");
        return workerList;
    }

    @Override
    public void changeWorkerStatus(String workerUsername, WorkerStatus workerStatus) {
        Optional<Worker> worker = Optional.of(WORKER_SERVICE.findByUsername(workerUsername).get());
        if (worker.isEmpty())
            throw new WorkerIsNotExistException("this worker does not exist!");
        if (worker.get().getStatus().equals(workerStatus))
            throw new WorkerIsHoldsExistException("this worker currently holds this status!");
        WORKER_SERVICE.changeWorkerStatus(workerUsername, workerStatus);
    }
}
