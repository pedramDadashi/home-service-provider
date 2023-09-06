package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.dto.request.UserRegistrationDTO;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {


    String register(UserRegistrationDTO dto);

    @Transactional
    String confirmToken(String token);

    String buildEmail(String name, String link);

    String addClient(UserRegistrationDTO clientRegistrationDTO);

    String addWorker(UserRegistrationDTO workerRegistrationDTO);
}
