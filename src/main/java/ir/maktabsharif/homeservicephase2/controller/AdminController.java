package ir.maktabsharif.homeservicephase2.controller;


import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/show-all-main-services")
    public List<MainServiceResponseDTO> findAllMainServices() {
        return adminService.findAllMainService();
    }

    @PostMapping("/add-main-service")
    public ResponseEntity<ProjectResponse> addMainService(@RequestBody MainServiceRequestDTO mainServiceRequestDTO) {
        return ResponseEntity.ok().body(adminService.createMainService(mainServiceRequestDTO));
    }

    @DeleteMapping("/delete-main-service/{name}")
    public ResponseEntity<ProjectResponse> deleteMainService(@PathVariable String name) {
        return ResponseEntity.ok().body(adminService.deleteMainService(name));
    }

    @GetMapping("/show-all-jobs")
    public List<JobResponseDTO> findAllJobs() {
        return adminService.findAllJob();
    }

    @GetMapping("/show-all-jobs-by-main-service/{mainServiceId}")
    public List<JobResponseDTO> findAllJobsByMainService(@PathVariable Long mainServiceId) {
        return adminService.findAllJobsByMainService(mainServiceId);
    }

    @PostMapping("/add-job")
    public ResponseEntity<ProjectResponse> addJob(@RequestBody JobRequestDTO jobRequestDTO) {
        return ResponseEntity.ok().body(adminService.addJob(jobRequestDTO));
    }

//    @DeleteMapping("/delete-job/{jobId}")
//    public ResponseEntity<ProjectResponse> deleteJob(@PathVariable Long jobId) {
//        return ResponseEntity.ok().body(adminService.deleteJob(jobId));
//    }

    @PutMapping("/edit-job-custom")
    public ResponseEntity<ProjectResponse> editJobCustom(@RequestBody UpdateJobDTO updateJobDTO) {
        return ResponseEntity.ok().body(adminService.editJobCustom(updateJobDTO));
    }

    @PostMapping("/add-worker-to-job/{jobId}/{workerId}")
    public ResponseEntity<ProjectResponse> addWorkerToJob(@PathVariable Long jobId, @PathVariable Long workerId) {
        return ResponseEntity.ok().body(adminService.addWorkerToJob(jobId, workerId));
    }

    @DeleteMapping("/delete-job-form-worker/{jobId}/{workerId}")
    public ResponseEntity<ProjectResponse> deleteJobFromWorker(@PathVariable Long jobId, @PathVariable Long workerId) {
        return ResponseEntity.ok().body(adminService.deleteJobFromWorker(jobId, workerId));
    }

    @PutMapping("/confirm-worker/{workerId}")
    public ResponseEntity<ProjectResponse> confirmWorker(@PathVariable Long workerId) {
        return ResponseEntity.ok().body(adminService.confirmWorker(workerId));
    }

    @PutMapping("/disable-worker/{workerId}")
    public ResponseEntity<ProjectResponse> disableWorker(@PathVariable Long workerId) {
        return ResponseEntity.ok().body(adminService.deActiveWorker(workerId));
    }

    @PostMapping("/filter-workers")
    public List<FilterWorkerResponseDTO> workerFilter(@RequestBody FilterWorkerDTO workerDTO) {
        return adminService.workerFilter(workerDTO);
    }

//    @PostMapping("/filter")
//    public List<FilterWorkerResponseDTO> userFilter(@RequestBody FilterWorkerDTO workerDTO) {
//        return adminService.userFilter(workerDTO);
//    }

    @PostMapping("/filter-clients")
    public List<FilterClientResponseDTO> clientFilter(@RequestBody FilterClientDTO clientDTO) {
        return adminService.clientFilter(clientDTO);
    }

//    @PutMapping("/check-expert-delay/{offerId}")
//    public void checkExpertDelayForDoingWork(@PathVariable Long offerId) {
//        adminService.checkExpertDelayForWork(offerId);

//    }

//    @GetMapping("/show-sub-services-expert/{subServiceId}")
//    public List<ExpertResponseDTO> viewSubServiceExperts(@PathVariable Long subServiceId) {
//        return adminService.showSubServicesExpert(subServiceId);
//    }
}
