package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.dto.request.AdminRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.ManagerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.UserRegistrationDTO;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface RegistrationService {

    @Transactional
    String confirmToken(String token);

    String addClient(UserRegistrationDTO clientRegistrationDTO);

    String addWorker(UserRegistrationDTO workerRegistrationDTO) throws IOException;

    String addAdmin(AdminRegistrationDTO adminRegistrationDTO);

    String addManager(ManagerRegistrationDTO managerRegistrationDTO);
}
