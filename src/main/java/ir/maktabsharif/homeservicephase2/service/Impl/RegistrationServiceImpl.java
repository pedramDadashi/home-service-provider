package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.dto.request.AdminRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.ManagerRegistrationDTO;
import ir.maktabsharif.homeservicephase2.dto.request.UserRegistrationDTO;
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
    public String addClient(UserRegistrationDTO dto) {
        validation.checkEmail(dto.getEmail());
        return clientService.addNewClient(dto);
    }

    @Override
    public String addWorker(UserRegistrationDTO dto) throws IOException {
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

        return "-*-*-*-*-*-*- Your email is confirmed -*-*-*-*-*-*-";
    }
//
//    @Override
//    public String buildEmail(String name, String link) {
//        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#800080\">\n" +
//               "\n" +
//               "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
//               "\n" +
//               "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
//               "    <tbody><tr>\n" +
//               "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
//               "        \n" +
//               "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
//               "          <tbody><tr>\n" +
//               "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
//               "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
//               "                  <tbody><tr>\n" +
//               "                    <td style=\"padding-left:10px\">\n" +
//               "                  \n" +
//               "                    </td>\n" +
//               "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
//               "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
//               "                    </td>\n" +
//               "                  </tr>\n" +
//               "                </tbody></table>\n" +
//               "              </a>\n" +
//               "            </td>\n" +
//               "          </tr>\n" +
//               "        </tbody></table>\n" +
//               "        \n" +
//               "      </td>\n" +
//               "    </tr>\n" +
//               "  </tbody></table>\n" +
//               "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
//               "    <tbody><tr>\n" +
//               "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
//               "      <td>\n" +
//               "        \n" +
//               "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
//               "                  <tbody><tr>\n" +
//               "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
//               "                  </tr>\n" +
//               "                </tbody></table>\n" +
//               "        \n" +
//               "      </td>\n" +
//               "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
//               "    </tr>\n" +
//               "  </tbody></table>\n" +
//               "\n" +
//               "\n" +
//               "\n" +
//               "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\"" +
//               " cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
//               "    <tbody><tr>\n" +
//               "      <td height=\"30\"><br></td>\n" +
//               "    </tr>\n" +
//               "    <tr>\n" +
//               "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
//               "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
//               "        \n" +
//               "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" +
//
//               "Hello " + name +
//
//               ",</p>" +
//               "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" +
//
//               "You registered an account on [HOME*SERVICE*PROVIDER] ,before being able to use your account" +
//               " you need to verify that this is your email address by clicking here:" +
//
//               " </p>" +
//               "<blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">" +
//               "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" +
//               " <a href=\"" + link + "\">Activate Now!</a> " +
//
//               "</p></blockquote>\n NOTE: The link is valid for 15 minutes and expires after the specified time." +
//               " <p>We're at your service :)</p>" +
//               "        \n" +
//               "      </td>\n" +
//               "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
//               "    </tr>\n" +
//               "    <tr>\n" +
//               "      <td height=\"30\"><br></td>\n" +
//               "    </tr>\n" +
//               "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
//               "\n" +
//               "</div></div>";
//    }

}
