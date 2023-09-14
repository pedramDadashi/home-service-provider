package ir.maktabsharif.homeservicephase2.controller;

import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.entity.user.Users;
import ir.maktabsharif.homeservicephase2.service.ClientService;
import ir.maktabsharif.homeservicephase2.util.Validation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final Validation validation;


    @PutMapping("/add-address")
    public ResponseEntity<ProjectResponse> addAddress(
            @RequestBody AddressDTO addressDTO, Authentication authentication) {
        return ResponseEntity.ok().body(clientService
                .addAddress(addressDTO, ((Users) authentication.getPrincipal()).getId()));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ProjectResponse> changePassword(
            @RequestBody ChangePasswordDTO changePasswordDTO, Authentication authentication) {
        return ResponseEntity.ok().body(clientService
                .editPassword(changePasswordDTO, ((Users) authentication.getPrincipal()).getId()));
    }

    @GetMapping("/show-all-main-services")
    public List<MainServiceResponseDTO> findAllMainService() {
        return clientService.showAllMainServices();
    }

    @GetMapping("/show-all-jobs-by-service/{mainServiceName}")
    public List<JobResponseDTO> findAllJobs(@PathVariable String mainServiceName) {
        return clientService.showJobs(mainServiceName);
    }

    @PostMapping("/submit-order")
    public ResponseEntity<ProjectResponse> submitOrder(
            @RequestBody SubmitOrderDTO submitOrderDTO, Authentication authentication) {
        return ResponseEntity.ok().body(clientService.addNewOrder(
                submitOrderDTO, ((Users) authentication.getPrincipal()).getId()));
    }

    @GetMapping("/show-all-orders")
    public List<FilterOrderResponseDTO> showAllOrders(Authentication authentication) {
        return clientService.showAllOrders(
                ((Users) authentication.getPrincipal()).getId());
    }

    @GetMapping("/filter-order/{orderStatus}")
    public List<FilterOrderResponseDTO> filterOrder(
            @PathVariable String orderStatus, Authentication authentication) {
        return clientService.filterOrder(orderStatus,
                ((Users) authentication.getPrincipal()).getId());
    }

    @GetMapping("/show-my-credit")
    public Long showClientCredit(Authentication authentication) {
        return clientService.getClientCredit(
                ((Users) authentication.getPrincipal()).getId());
    }

    @GetMapping("/show-all-offer-by-score/{orderId}")
    public List<OfferResponseDTO> showAllOfferForOrderByWorkerScore(
            @PathVariable Long orderId, Authentication authentication) {
        return clientService.findOfferListByOrderIdBasedOnWorkerScore(
                orderId, ((Users) authentication.getPrincipal()));
    }

    @GetMapping("/show-all-offer-by-price/{orderId}")
    public List<OfferResponseDTO> showAllOfferForOrderByProposedPrice(
            @PathVariable Long orderId, Authentication authentication) {
        return clientService.findOfferListByOrderIdBasedOnProposedPrice(
                orderId, ((Users) authentication.getPrincipal()));
    }

    @PutMapping("/choose-offer-for-order/{offerId}")
    public ResponseEntity<ProjectResponse> chooseWorkerForOrder(
            @PathVariable Long offerId, Authentication authentication) {
        return ResponseEntity.ok().body(clientService.chooseWorkerForOrder(
                offerId, ((Users) authentication.getPrincipal())));
    }

    @PutMapping("/order-started/{orderId}")
    public ResponseEntity<ProjectResponse> orderStarted(
            @PathVariable Long orderId, Authentication authentication) {
        return ResponseEntity.ok().body(clientService.changeOrderStatusToStarted(
                orderId, ((Users) authentication.getPrincipal())));
    }

    @PutMapping("/order-doned/{orderId}")
    public ResponseEntity<ProjectResponse> orderDoned(
            @PathVariable Long orderId, Authentication authentication) {
        return ResponseEntity.ok().body(clientService.changeOrderStatusToDone(
                orderId, ((Users) authentication.getPrincipal())));
    }

    @PostMapping("/register-comment")
    public ResponseEntity<ProjectResponse> registerComment(
            @RequestBody CommentRequestDTO commentRequestDTO, Authentication authentication) {
        return ResponseEntity.ok().body(clientService.registerComment(
                commentRequestDTO, ((Users) authentication.getPrincipal())));
    }

    @PutMapping("/paid-in-app-credit/{orderId}")
    public ResponseEntity<ProjectResponse> payByInApp(
            @PathVariable Long orderId, Authentication authentication) {
        return ResponseEntity.ok().body(clientService.paidByInAppCredit(
                orderId, ((Users) authentication.getPrincipal())));
    }

    @Transactional
    @GetMapping("/pay-online-payment/{orderId}")
    public ModelAndView payByOnlinePayment(
            @PathVariable Long orderId, Model model, Authentication authentication) {
        return clientService.payByOnlinePayment(
                orderId, ((Users) authentication.getPrincipal()), model);
    }

    @Transactional
    @GetMapping("/increase-account-balance/{price}")
    public ModelAndView increaseAccountBalance(
            @PathVariable Long price, Model model, Authentication authentication) {
        return clientService.increaseAccountBalance(
                price, ((Users) authentication.getPrincipal()).getId(), model);
    }

    @PostMapping("/send-payment-info")
    public ResponseEntity<ProjectResponse> paymentInfo(
            @ModelAttribute @Validated PaymentRequestDTO dto) {
        validation.checkPaymentRequest(dto);
        return ResponseEntity.ok().body(clientService.changeOrderStatusToPaidByOnlinePayment(
                dto.getClientIdOrderIdDTO()));
    }
    @PostMapping("/send-increase-balance-info")
    public ResponseEntity<ProjectResponse> increaseBalanceInfo(
            @ModelAttribute @Validated BalanceRequestDTO dto) {
        validation.checkBalanceRequest(dto);
        return ResponseEntity.ok().body(clientService.increaseClientCredit(
                dto.getClientIdPriceDTO()));
    }

}
