package ir.maktabsharif.homeservicephase2.service.Impl;

import ir.maktabsharif.homeservicephase2.entity.user.enums.Role;
import ir.maktabsharif.homeservicephase2.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;


    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;

    }


    //    @Override
//    @Async
//    public void sendEmail(String to, String email) {
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//            helper.setText(email, true);
//            helper.setTo(to);
//            helper.setSubject("Confirm your email");
//            helper.setFrom("pedadashii@gmail.com");
//            mailSender.send(mimeMessage);
//        } catch (MessagingException e) {
//            throw new IllegalArgumentException("Failed to send email for: " + email);
//        }
//    }
    @Override
    public SimpleMailMessage createEmail(String toEmail, String firstName, String token, Role role) {
        String accountType = role.name();
        String emailText = "Hi " + firstName +
                           """
                                                   
                                   You registered an account on [HOME*SERVICE*PROVIDER]
                                   before being able to use your account you need to verify that this is your email address by clicking below link.
                                                   
                                   NOTE: The link is valid for 15 minutes and expires after the specified time.
                                                   
                                                                 
                                   link:http://localhost:8080/registration/confirm?token=""" + token +
                           """
                                     
                                                                     
                                   We're at your service :)
                                   """;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setFrom("pedadashii@gmail.com");
        mailMessage.setSubject("Complete " + accountType + " Registration!");
        mailMessage.setText(emailText);
        return mailMessage;
    }

    @Override
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }
}
