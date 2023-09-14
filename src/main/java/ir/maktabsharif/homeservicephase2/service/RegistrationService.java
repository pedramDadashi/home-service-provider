package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.dto.request.AdminRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.ClientRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.ManagerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.WorkerRegistrationDTO;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface RegistrationService {

    @Transactional
    String confirmToken(String token);

    String addClient(ClientRegistrationDTO clientRegistrationDTO);

    String addWorker(WorkerRegistrationDTO workerRegistrationDTO) throws IOException;

    String addAdmin(AdminRegistrationDTO adminRegistrationDTO);

    String addManager(ManagerRegistrationDTO managerRegistrationDTO);
}
