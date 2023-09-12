package ir.maktabsharif.homeservicephase2.controller;


import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.user.Admin;
import ir.maktabsharif.homeservicephase2.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/manager")
public class ManagerController extends AdminController {

    private final AdminService adminService;

    public ManagerController(AdminService adminService) {
        super(adminService);
        this.adminService = adminService;
    }


    @GetMapping("/show-app-credit")
    public Long showAppCredit(Authentication authentication) {
        Admin manager = (Admin) authentication.getPrincipal();
        return adminService.findById(manager.getId()).get().getCredit();
    }

    @Override
    public List<MainServiceResponseDTO> findAllMainServices() {
        return super.findAllMainServices();
    }

    @Override
    public ResponseEntity<ProjectResponse> addMainService(
            MainServiceRequestDTO mainServiceRequestDTO) {
        return super.addMainService(mainServiceRequestDTO);
    }

    @Override
    public ResponseEntity<ProjectResponse> deleteMainService(String name) {
        return super.deleteMainService(name);
    }

    @Override
    public List<JobResponseDTO> findAllJobs() {
        return super.findAllJobs();
    }

    @Override
    public List<JobResponseDTO> findAllJobsByMainService(Long mainServiceId) {
        return super.findAllJobsByMainService(mainServiceId);
    }

    @Override
    public ResponseEntity<ProjectResponse> addJob(JobRequestDTO jobRequestDTO) {
        return super.addJob(jobRequestDTO);
    }

    @Override
    public ResponseEntity<ProjectResponse> editJobCustom(
            UpdateJobDTO updateJobDTO) {
        return super.editJobCustom(updateJobDTO);
    }

    @Override
    public ResponseEntity<ProjectResponse> addWorkerToJob(
            Long jobId, Long workerId) {
        return super.addWorkerToJob(jobId, workerId);
    }

    @Override
    public ResponseEntity<ProjectResponse> deleteJobFromWorker(
            Long jobId, Long workerId) {
        return super.deleteJobFromWorker(jobId, workerId);
    }

    @Override
    public ResponseEntity<ProjectResponse> confirmWorker(
            Long workerId) {
        return super.confirmWorker(workerId);
    }

    @Override
    public ResponseEntity<ProjectResponse> disableWorker(
            Long workerId) {
        return super.disableWorker(workerId);
    }

    @Override
    public List<FilterUserResponseDTO> userFilter(FilterUserDTO userDTO) {
        return super.userFilter(userDTO);
    }

    @Override
    public List<FilterOrderResponseDTO> orderFilter(FilterOrderDTO orderDTO) {
        return super.orderFilter(orderDTO);
    }
}
