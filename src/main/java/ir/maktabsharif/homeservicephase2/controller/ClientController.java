package ir.maktabsharif.homeservicephase2.controller;

import cn.apiclub.captcha.Captcha;
import ir.maktabsharif.homeservicephase2.dto.request.*;
import ir.maktabsharif.homeservicephase2.dto.response.*;
import ir.maktabsharif.homeservicephase2.service.ClientService;
import ir.maktabsharif.homeservicephase2.util.CaptchaUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/signup")
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
    public ResponseEntity<ProjectResponse> submitOrder(@RequestBody @Validated SubmitOrderDTO submitOrderDTO) {
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

    @PutMapping("/chose-offer-for-order/{offerId}")
    public ResponseEntity<ProjectResponse> choseWorkerForOrder(@PathVariable Long offerId) {
        return ResponseEntity.ok().body(clientService.choseWorkerForOrder(offerId));
    }

    @PutMapping("/order-start/{orderId}")
    public ResponseEntity<ProjectResponse> orderStarted(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(clientService.changeOrderStatusToStarted(orderId));
    }

    @PutMapping("/order-done/{orderId}")
    public ResponseEntity<ProjectResponse> orderDone(@PathVariable Long orderId) {
        return ResponseEntity.ok().body(clientService.changeOrderStatusToDone(orderId));
    }

    @PostMapping("/add-comment")
    @ResponseBody
    public ResponseEntity<ProjectResponse> addComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        return ResponseEntity.ok().body(clientService.addComment(commentRequestDTO));
    }

    @PutMapping("/paid-in-app-credit")
    public ResponseEntity<ProjectResponse> payByInApp(@RequestBody @Validated ClientIdOrderIdDTO dto) {
        ProjectResponse projectResponse = clientService.paidByInAppCredit(dto);
        return ResponseEntity.ok().body(projectResponse);
    }

    private void setupCaptchaAndPrice(PaymentPageDTO dto) {
        Captcha captcha = CaptchaUtil.createCaptcha(200, 50);
        dto.setHidden(captcha.getAnswer());
        dto.setCaptcha("");
        dto.setImage(CaptchaUtil.encodeBase64(captcha));
    }

    @Transactional
    @GetMapping("/pay-with-order-id/{clientId}/{orderId}")
    public ModelAndView pay(@PathVariable Long clientId, @PathVariable Long orderId, Model model) {
        PaymentPageDTO paymentPageDTO = new PaymentPageDTO();
        ClientIdOrderIdDTO clientIdOrderIdDTO =new ClientIdOrderIdDTO(clientId,orderId);
        paymentPageDTO.setClientIdOrderIdDTO(clientIdOrderIdDTO);
        paymentPageDTO.setPrice(clientService.paymentPriceCalculator(orderId, clientId));
        setupCaptchaAndPrice(paymentPageDTO);
        model.addAttribute("dto", paymentPageDTO);
        return new ModelAndView("payment");
    }


    @PostMapping("/send-payment-info")
    public ResponseEntity<ProjectResponse> paymentInfo(@ModelAttribute @Validated PaymentRequestDTO dto) {
        clientService.paymentRequestValidation(dto);
        ProjectResponse projectResponse = clientService.changeOrderStatusToPaidByOnlinePayment(
                dto.getClientIdOrderIdDTO()
        );
        return ResponseEntity.ok().body(projectResponse);
    }

}
