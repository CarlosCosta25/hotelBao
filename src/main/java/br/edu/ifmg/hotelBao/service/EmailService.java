package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.EmailDTO;
import br.edu.ifmg.hotelBao.service.exceptions.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailDTO dto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(dto.getTo());
            message.setSubject(dto.getSubject());
            message.setText(dto.getBody());
            mailSender.send(message);
        }
        catch (MailSendException e) {
            throw new EmailException("Falha ao enviar e-mail: " + e.getMessage());
        }
    }
}