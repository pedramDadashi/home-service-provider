package ir.maktabsharif.homeservicephase2.controller;


import ir.maktabsharif.homeservicephase2.dto.request.ChangePasswordDTO;
import ir.maktabsharif.homeservicephase2.dto.request.ChooseMainServiceDTO;
import ir.maktabsharif.homeservicephase2.dto.request.OfferRequestDTO;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.user.Users;
import ir.maktabsharif.homeservicephase2.entity.user.Worker;
import ir.maktabsharif.homeservicephase2.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PutMapping("/change-password")
    public ResponseEntity<ProjectResponse> changePassword(
            @RequestBody ChangePasswordDTO changePasswordDTO, Authentication authentication) {
        return ResponseEntity.ok().body(workerService
                .editPassword(changePasswordDTO,
                        ((Users) authentication.getPrincipal()).getId()));
    }

    @GetMapping("/show-all-main-services")
    public List<MainServiceResponseDTO> findAllMainService() {
        return workerService.showAllMainServices();
    }

    @GetMapping("/show-all-jobs-by-service")
    public List<JobResponseDTO> findAllJobs(
            @RequestBody ChooseMainServiceDTO chooseMainServiceDTO) {
        return workerService.showJobs(chooseMainServiceDTO);
    }

    @GetMapping("/show-related-orders")
    public List<LimitedOrderResponseDTO> viewOrdersRelatedToWorker(
            Authentication authentication) {
        return workerService.showRelatedOrders(((Users) authentication.getPrincipal()).getId());
    }

    @PostMapping("/submit-offer-for-order")
    public ResponseEntity<ProjectResponse> submitOfferForOrder(
            @RequestBody OfferRequestDTO offerRequestDTO, Authentication authentication) {
        return ResponseEntity.ok().body(workerService.submitAnOffer(
                offerRequestDTO, ((Users) authentication.getPrincipal()).getId()));
    }

    @GetMapping("/show-worker-rate")
    public double viewWorkerScore(Authentication authentication) {
        Worker authenticatedWorker = (Worker) authentication.getPrincipal();
        System.out.println(authenticatedWorker.getScore());
        return workerService.getWorkerRate(authenticatedWorker.getId());
    }

    @GetMapping("/show-worker-credit")
    public Long viewWorkerCredit(Authentication authentication) {
        Worker authenticatedWorker = (Worker) authentication.getPrincipal();
        return workerService.getWorkerCredit(authenticatedWorker.getId());
    }

    @GetMapping("/show-all-offers-waiting")
    public List<OfferResponseDTO> viewAllWaitingOffers(Authentication authentication) {
        Worker authenticatedWorker = (Worker) authentication.getPrincipal();
        return workerService.showAllOffersWaiting(authenticatedWorker.getId());
    }

    @GetMapping("/show-all-offers-accepted")
    public List<OfferResponseDTO> viewAllAcceptedOffers(Authentication authentication) {
        Worker authenticatedWorker = (Worker) authentication.getPrincipal();
        return workerService.showAllOffersAccepted(authenticatedWorker.getId());
    }

    @GetMapping("/show-all-offers-rejected")
    public List<OfferResponseDTO> viewAllRejectedOffers(Authentication authentication) {
        Worker authenticatedWorker = (Worker) authentication.getPrincipal();
        return workerService.showAllOffersRejected(authenticatedWorker.getId());
    }


}
