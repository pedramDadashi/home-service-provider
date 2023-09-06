package ir.maktabsharif.homeservicephase2.controller;

import ir.maktabsharif.homeservicephase2.dto.request.UserRegistrationDTO;
import ir.maktabsharif.homeservicephase2.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping( "/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
//http://localhost:8080/registration/signup-client/confirm?token=
    @PostMapping("/signup-client")
    public String clientSingUp(
            @RequestBody UserRegistrationDTO clientRegistrationDTO) {
        return registrationService.addClient(clientRegistrationDTO);
    }

    @PostMapping("/signup-worker")
    public String workerSingUp(
            @ModelAttribute UserRegistrationDTO workerRegistrationDTO) {
        return registrationService.addWorker(workerRegistrationDTO);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}


