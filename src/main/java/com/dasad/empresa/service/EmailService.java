package com.dasad.empresa.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String email, String resetUrl, String userName) throws MessagingException {
        log.info("Enviando e-mail de redefinição de senha para: {}", email);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String htmlMsg = "<h1>Esqueceu sua senha?</h1>"
                + "<p>Olá " + userName + ",</p>"
                + "<p>Recebemos sua solicitação para redefinir a senha de acesso ao Sistema Empresa.</p>"
                + "<p>Para prosseguir, clique no botão abaixo:</p>"
                + "<a href=\"" + resetUrl + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;\">Redefinir Senha</a>"
                + "<p>Caso não consiga clicar no botão acima, copie e cole este endereço no seu navegador: <a href=\"" + resetUrl + "\">" + resetUrl + "</a></p>"
                + "<p>Atenciosamente,<br>Equipe Suporte do Sistema Empresa.</p>";

        helper.setTo(email);
        helper.setSubject("Redefinição de Senha");
        helper.setText(htmlMsg, true);

        mailSender.send(message);
    }
}