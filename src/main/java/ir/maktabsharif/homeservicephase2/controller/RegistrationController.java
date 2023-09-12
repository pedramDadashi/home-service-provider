package ir.maktabsharif.homeservicephase2.controller;

import ir.maktabsharif.homeservicephase2.dto.request.AdminRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.ManagerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.UserRegistrationDTO;
import ir.maktabsharif.homeservicephase2.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/signup-admin")
    public String adminSingUp(
            @RequestBody AdminRegistrationDTO adminRegistrationDTO) {
        return registrationService.addAdmin(adminRegistrationDTO);
    }
    @PostMapping("/signup-manager")
    public String managerSingUp(
            @RequestBody ManagerRegistrationDTO managerRegistrationDTO) {
        return registrationService.addManager(managerRegistrationDTO);
    }

    @PostMapping("/signup-client")
    public String clientSingUp(
            @RequestBody UserRegistrationDTO clientRegistrationDTO) {
        return registrationService.addClient(clientRegistrationDTO);
    }

    @PostMapping("/signup-worker")
    public String workerSingUp(
            @ModelAttribute UserRegistrationDTO workerRegistrationDTO) throws IOException {
        return registrationService.addWorker(workerRegistrationDTO);
    }

    /*
    http://localhost:8080/registration/confirm?token=
    */
    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}


