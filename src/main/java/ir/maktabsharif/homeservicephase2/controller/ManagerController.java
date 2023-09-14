package ir.maktabsharif.homeservicephase2.controller;


import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.user.Admin;
import ir.maktabsharif.homeservicephase2.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        return adminService.findById(((Admin) authentication.getPrincipal()).getId()).get().getCredit();
    }

    @GetMapping("/show-all-main-services")
    public List<MainServiceResponseDTO> findAllMainServices() {
        return super.findAllMainServices();
    }

    @PostMapping("/add-main-service")
    public ResponseEntity<ProjectResponse> addMainService(
            @RequestBody MainServiceRequestDTO mainServiceRequestDTO) {
        return super.addMainService(mainServiceRequestDTO);
    }

    @DeleteMapping("/delete-main-service/{name}")
    public ResponseEntity<ProjectResponse> deleteMainService(@PathVariable String name) {
        return super.deleteMainService(name);
    }

    @GetMapping("/show-all-jobs")
    public List<JobResponseDTO> findAllJobs() {
        return super.findAllJobs();
    }

    @GetMapping("/show-all-jobs-by-main-service/{mainServiceId}")
    public List<JobResponseDTO> findAllJobsByMainService(@PathVariable Long mainServiceId) {
        return super.findAllJobsByMainService(mainServiceId);
    }

    @PostMapping("/add-job")
    public ResponseEntity<ProjectResponse> addJob(@RequestBody JobRequestDTO jobRequestDTO) {
        return super.addJob(jobRequestDTO);
    }

    @PutMapping("/edit-job-custom")
    public ResponseEntity<ProjectResponse> editJobCustom(@RequestBody UpdateJobDTO updateJobDTO) {
        return super.editJobCustom(updateJobDTO);
    }

    @PostMapping("/add-worker-to-job/{jobId}/{workerId}")
    public ResponseEntity<ProjectResponse> addWorkerToJob(@PathVariable Long jobId, @PathVariable Long workerId) {
        return super.addWorkerToJob(jobId, workerId);
    }

    @DeleteMapping("/delete-job-form-worker/{jobId}/{workerId}")
    public ResponseEntity<ProjectResponse> deleteJobFromWorker(@PathVariable Long jobId, @PathVariable Long workerId) {
        return super.deleteJobFromWorker(jobId, workerId);
    }

    @PutMapping("/confirm-worker/{workerId}")
    public ResponseEntity<ProjectResponse> confirmWorker(@PathVariable Long workerId) {
        return super.confirmWorker(workerId);
    }

    @PutMapping("/disable-worker/{workerId}")
    public ResponseEntity<ProjectResponse> disableWorker(@PathVariable Long workerId) {
        return super.disableWorker(workerId);
    }

    @PostMapping("/filter-users")
    public List<FilterUserResponseDTO> userFilter(@RequestBody FilterUserDTO userDTO) {
        return super.userFilter(userDTO);
    }

    @PostMapping("/filter-order")
    public List<FilterOrderResponseDTO> orderFilter(@RequestBody FilterOrderDTO orderDTO) {
        return super.orderFilter(orderDTO);
    }
}
