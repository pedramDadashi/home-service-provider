package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendEmail(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("pedadashii@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalArgumentException("Failed to send email for: " + email);
        }
    }

    @Override
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }

    @Override
    public SimpleMailMessage createEmail(String toEmail, String token, String accountType) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setFrom("pedadashii@gmail.com");
        if (accountType.equals("client")) {
            mailMessage.setSubject("Complete Customer Registration!");
            mailMessage.setText("To confirm your account, please click here : "
                                + "http://localhost:8080/registration/signup-client/confirm?token="
                                + token);
        } else if (accountType.equals("worker")) {
            mailMessage.setSubject("Complete Expert Registration!");
            mailMessage.setText("To confirm your account, please click here : "
                                + "http://localhost:8080/registration/singup-worker/confirm?token="
                                + token);
        }
        return mailMessage;
    }
}
