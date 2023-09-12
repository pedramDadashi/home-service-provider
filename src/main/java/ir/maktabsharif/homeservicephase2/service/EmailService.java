package ir.maktabsharif.homeservicephase2.service;

import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

//    void sendEmail(String to, String email);

    void sendEmail(SimpleMailMessage email);

    SimpleMailMessage createEmail(String toEmail,String firstName, String confirmationToken, Role role);

}
