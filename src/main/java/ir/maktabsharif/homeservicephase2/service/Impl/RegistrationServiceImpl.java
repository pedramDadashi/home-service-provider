package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.dto.request.AdminRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.ClientRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.ManagerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.WorkerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.entity.user.Users;
import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import ir.maktabsharif.homeservicephase2.entity.user.enums.WorkerStatus;
import ir.maktabsharif.homeservicephase2.exception.ValidationTokenException;
import ir.maktabsharif.homeservicephase2.exception.VerifyCodeException;
import ir.maktabsharif.homeservicephase2.mapper.ClientMapper;
import ir.maktabsharif.homeservicephase2.security.token.entity.Token;
import ir.maktabsharif.homeservicephase2.security.token.service.TokenService;
import ir.maktabsharif.homeservicephase2.service.*;
import ir.maktabsharif.homeservicephase2.util.Validation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final String ADMIN_VERIFY_CODE = "admin_provider_4454";
    private static final String MANAGER_VERIFY_CODE = "manager_provider_4454";

    private final ClientService clientService;
    private final WorkerService workerService;
    private final AdminService adminService;
    private final ManagerService managerService;
    private final Validation validation;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final ClientMapper clientMapper;

    public RegistrationServiceImpl(ClientService clientService, WorkerService workerService,
                                   AdminService adminService, ManagerService managerService, Validation validation,
                                   TokenService tokenService, EmailService emailService,
                                   ClientMapper clientMapper) {
        this.clientService = clientService;
        this.workerService = workerService;
        this.adminService = adminService;
        this.managerService = managerService;
        this.validation = validation;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.clientMapper = clientMapper;
    }

    @Override
    public String addManager(ManagerRegistrationDTO dto) {
        if (!(dto.getManagerVerifyCode().equals(MANAGER_VERIFY_CODE)))
            throw new VerifyCodeException(
                    "manager verify code invalid! call the INFORMATICS.");
        validation.checkEmail(dto.getEmail());
        return managerService.addNewManager(dto);
    }

    @Override
    public String addAdmin(AdminRegistrationDTO dto) {
        if (!(dto.getVerifyCode().equals(ADMIN_VERIFY_CODE)))
            throw new VerifyCodeException(
                    "verify code invalid! call the MANAGER.");
        validation.checkEmail(dto.getEmail());
        return adminService.addNewAdmin(dto);
    }

    @Override
    public String addClient(ClientRegistrationDTO dto) {
        validation.checkEmail(dto.getEmail());
        return clientService.addNewClient(dto);
    }

    @Override
    public String addWorker(WorkerRegistrationDTO dto) throws IOException {
        validation.checkEmail(dto.getEmail());
        return workerService.addNewWorker(dto);
    }

    @Transactional
    @Override
    public String confirmToken(String token) {
        Optional<Token> confirmToken = tokenService.getToken(token);
        if (confirmToken.isEmpty()) {
            throw new ValidationTokenException("Token not found!");
        }
        if (confirmToken.get().getConfirmedAt() != null) {
            throw new ValidationTokenException("Email is already confirmed");
        }
        if ((confirmToken.get().getExpiresAt()).isBefore(LocalDateTime.now())) {
            throw new ValidationTokenException("Token is already expired!");
        }
        Users users = confirmToken.get().getUsers();
        if ((users.getRole()).equals(Role.WORKER))
            workerService.findById(users.getId()).get().setStatus(WorkerStatus.AWAITING);
        confirmToken.get().getUsers().setIsActive(true);
        tokenService.setConfirmedAt(token);

        return "-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-" +
               "-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-" +
               "-*-*-*-*-*-*-* Your email is confirmed *-*-*-*-*-*-*-" +
               "-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-" +
               "-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-";
    }
}
