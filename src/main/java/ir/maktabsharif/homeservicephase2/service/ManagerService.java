package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.dto.request.ManagerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.entity.user.Admin;

public interface ManagerService extends UsersService<Admin> {

    Admin findManager();

    String addNewManager(ManagerRegistrationDTO dto);
//    @Override
//    void save(Admin admin);
//
//    @Override

//    List<Admin> findAll();

    //    ProjectResponse createMainService(MainServiceRequestDTO msDTO);
//
//    ProjectResponse deleteMainService(String name);
//
//    ProjectResponse addJob(JobRequestDTO jobRequestDTO);
//
//    ProjectResponse deleteJob(String name);
//
//    ProjectResponse addWorkerToJob(Long jobId, Long workerId);
//
//    ProjectResponse deleteJobFromWorker(Long jobId, Long workerId);
//
//    List<MainServiceResponseDTO> findAllMainService();
//
//    List<JobResponseDTO> findAllJob();
//
//    ProjectResponse editJobCustom(UpdateJobDTO updateJobDTO);
//
//    List<WorkerResponseDTO> findAllWorkers();
//
//    ProjectResponse confirmWorker(Long workerId);
//
//    List<JobResponseDTO> findAllJobsByMainService(Long mainServiceId);
//
//    ProjectResponse deActiveWorker(Long workerId);
//
//    List<FilterWorkerResponseDTO> workerFilter(FilterWorkerDTO workerDTO);
//
//    List<FilterClientResponseDTO> clientFilter(FilterClientDTO clientDTO);
//
//    String addNewAdmin(AdminRegistrationDTO dto);

    //    List<FilterOrderResponseDTO> ordersFilter(FilterOrderDTO filterOrderDTO);
//    List<FilterWorkerResponseDTO> userFilter(FilterWorkerDTO workerDTO);

}
