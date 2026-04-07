package service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

	
    private final String username;
    private final String password;

    /**
     * @param username Gmail address to send from
     * @param password Gmail app password
     */
    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Sends an email to a recipient.
     * @param toEmail recipient email address
     * @param subject email subject
     * @param body    email body
     */
    public void sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
            System.out.println("[EMAIL SENT] To: " + toEmail);
        } catch (MessagingException e) {
            System.out.println("[EMAIL FAILED] To: " + toEmail + " | " + e.getMessage());
        }
    }
}
