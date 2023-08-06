package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.entity.job.Job;
import ir.maktabsharif.homeservicephase2.entity.service.MainService;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import java.util.List;

public interface AdminService {

    void createMainService(String name);

    void deleteMainService(String name);

    void addJob(String mainServiceName, String jobName, Long basePrice, String description);

    void deleteJob(String name);

    void addWorkerToJob(String jobName, String workerEmail);

    void deleteWorkerFromJob(String jobName, String workerEmail);

    List<MainService> findAllMainService();

    List<Job> findAllJob();

    void editJob(String jobName, Long basePrice, String description);

    List<Worker> findAllWorkers();

    void changeWorkerStatus(String workerUsername, WorkerStatus workerStatus);

}
