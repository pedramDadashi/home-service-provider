package ir.maktabsharif.homeservicephase2.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendEmail(String to, String email);

    void sendEmail(SimpleMailMessage email);

    SimpleMailMessage createEmail(String toEmail, String confirmationToken, String accountType);

}
