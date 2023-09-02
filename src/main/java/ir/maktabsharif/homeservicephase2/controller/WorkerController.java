package ir.maktabsharif.homeservicephase2.controller;


import ir.maktabsharif.homeservicephase2.dto.request.ChangePasswordDTO;
import ir.maktabsharif.homeservicephase2.dto.request.LoginDTO;
import ir.maktabsharif.homeservicephase2.dto.request.OfferRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.request.UserRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping("/signup")
    public ResponseEntity<ProjectResponse> singUp(@ModelAttribute UserRegistrationDTO workerRegistrationDTO) {
        return ResponseEntity.ok().body(workerService.addWorker(workerRegistrationDTO));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ProjectResponse> login(@RequestBody LoginDTO workerLoginDto) {
        return ResponseEntity.ok().body(workerService.loginWorker(workerLoginDto));
    }

    @PutMapping("/change-password")
    @ResponseBody
    public ResponseEntity<ProjectResponse> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return ResponseEntity.ok().body(workerService.editPassword(changePasswordDTO));
    }

    @GetMapping("/show-all-main-services")
    public List<MainServiceResponseDTO> findAllMainService() {
        return workerService.showAllMainServices();
    }

    @GetMapping("/show-all-jobs-by-service/{mainServiceId}")
    public List<JobResponseDTO> findAllJobs(@PathVariable Long mainServiceId) {
        return workerService.showJobs(mainServiceId);
    }

    @GetMapping("/show-related-orders/{workerId}")
    public List<OrderResponseDTO> viewOrdersRelatedToWorker(@PathVariable Long workerId) {
        return workerService.showRelatedOrders(workerId);
    }

    @PostMapping("/submit-offer-for-order")
    public ResponseEntity<ProjectResponse> submitOfferForOrder(@RequestBody OfferRequestDTO offerRequestDTO) {
        return ResponseEntity.ok().body(workerService.submitAnOffer(offerRequestDTO));
    }

    @GetMapping("/show-worker-rate/{workerId}")
    public double viewWorkerScore(@PathVariable Long workerId) {
        return workerService.getWorkerRate(workerId);
    }

    @GetMapping("/show-worker-credit/{workerId}")
    public Long viewWorkerCredit(@PathVariable Long workerId) {
        return workerService.getWorkerCredit(workerId);
    }

    @GetMapping("/show-all-offers-waiting/{workerId}")
    public List<OfferResponseDTO> viewAllWaitingOffers(@PathVariable Long workerId) {
        return workerService.showAllOffersWaiting(workerId);
    }

    @GetMapping("/show-all-offers-accepted/{workerId}")
    public List<OfferResponseDTO> viewAllAcceptedOffers(@PathVariable Long workerId) {
        return workerService.showAllOffersAccepted(workerId);
    }

    @GetMapping("/show-all-offers-rejected/{workerId}")
    public List<OfferResponseDTO> viewAllRejectedOffers(@PathVariable Long workerId) {
        return workerService.showAllOffersRejected(workerId);
    }
}
