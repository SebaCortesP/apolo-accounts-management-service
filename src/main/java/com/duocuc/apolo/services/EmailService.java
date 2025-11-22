package com.duocuc.apolo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendNewPassword(String to, String newPassword) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Recuperación de contraseña");
        msg.setText("Tu nueva contraseña es: " + newPassword);
        mailSender.send(msg);
    }
}
