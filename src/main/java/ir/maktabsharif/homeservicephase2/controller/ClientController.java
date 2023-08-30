package ir.maktabsharif.homeservicephase2.controller;

import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

//    private CaptchaService captchaService;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<ProjectResponse> singUp(@RequestBody UserRegistrationDTO clientRegistrationDTO) {
        return ResponseEntity.ok().body(clientService.addClient(clientRegistrationDTO));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ProjectResponse> login(@RequestBody LoginDTO clientLoginDto) {
        return ResponseEntity.ok().body(clientService.loginClient(clientLoginDto));
    }

    @PutMapping("/change-password")
    @ResponseBody
    public ResponseEntity<ProjectResponse> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return ResponseEntity.ok().body(clientService.editPassword(changePasswordDTO));
    }

    @GetMapping("/show-all-main-services")
    public List<MainServiceResponseDTO> findAllMainService() {
        return clientService.showAllMainServices();
    }

    @GetMapping("/show-all-jobs-by-service/{mainServiceId}")
    public List<JobResponseDTO> findAllJobs(@PathVariable Long mainServiceId) {
        return clientService.showJobs(mainServiceId);
    }

    @PostMapping("/submit-order")
    @ResponseBody
    public ResponseEntity<ProjectResponse> submitOrder(@RequestBody SubmitOrderDTO submitOrderDTO) {
        return ResponseEntity.ok().body(clientService.addNewOrder(submitOrderDTO));
    }

    @GetMapping("/show-all-orders/{clientId}")
    public List<OrderResponseDTO> showAllOrders(@PathVariable Long clientId) {
        return clientService.showAllOrders(clientId);
    }

    @GetMapping("/show-all-offer-by-score/{orderId}")
    public List<OfferResponseDTO> showAllOfferForOrderByWorkerScore(@PathVariable Long orderId) {
        return clientService.findOfferListByOrderIdBasedOnWorkerScore(orderId);
    }

    @GetMapping("/show-all-offer-by-price/{orderId}")
    public List<OfferResponseDTO> showAllOfferForOrderByProposedPrice(@PathVariable Long orderId) {
        return clientService.findOfferListByOrderIdBasedOnProposedPrice(orderId);
    }

    @PutMapping("/chose-worker-for-order/{offerId}")
    @ResponseBody
    public ResponseEntity<ProjectResponse> choseWorkerForOrder(@PathVariable Long offerId) {
        return ResponseEntity.ok().body(clientService.choseWorkerForOrder(offerId));
    }

    @PutMapping("/order-start/{orderId}")
    @ResponseBody
    public ResponseEntity<ProjectResponse> orderStarted(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(clientService.changeOrderStatusToStarted(orderId));
    }

    @PutMapping("/order-done/{orderId}")
    @ResponseBody
    public ResponseEntity<ProjectResponse> orderDone(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(clientService.changeOrderStatusToDone(orderId));
    }

    @PostMapping("/add-comment")
    @ResponseBody
    public ResponseEntity<ProjectResponse> addComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        return ResponseEntity.ok().body(clientService.addComment(commentRequestDTO));
    }

//    @PutMapping("/pay-by-credit/{orderId}/{customerId}/{expertId}/{amount}")
//    @ResponseBody
//    public void payFromCredit(@PathVariable Long orderId, @PathVariable Long customerId, @PathVariable Long expertId, @PathVariable Long amount) {
//        customerService.payByCredit(orderId, customerId, expertId, amount);
//    }
//
//    @GetMapping("/payment")
//    public String showPaymentPage(Model model) {
//        // create model object to store form data
//        CardDTO card = new CardDTO();
//        model.addAttribute("card", card);
//        return "payment";
//    }
//
//    @PostMapping("/payment/pay")
//    @ResponseBody
//    private String pay(@Valid final CardDTO cardDto, final HttpServletRequest request) {
//        final String response = request.getParameter("g-recaptcha-response");
//        captchaService.processResponse(response);
//
//        return "successful payment";
//    }

}
