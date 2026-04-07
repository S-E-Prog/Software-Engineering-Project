package service;
import java.util.ArrayList;
import java.util.Properties;
import domain.user;
import jakarta.mail.*;
import jakarta.mail.internet.*;
/**
 * Handles email notifications to users.
 * @author sabre
 * 
 */
public class EmailNotification implements NotificationObserver{

	 private final EmailService emailService;

	 
	    /**
	     * @param emailService
	     */
	    public EmailNotification(EmailService emailService) {
	        this.emailService = emailService;
	    }

	    @Override
	    public void update(ArrayList<user> users, String message) {
	        for (user u : users) {
	            emailService.sendEmail(
	                u.getEmail(),
	                "Appointment Notification",
	                "Dear " + u.getName() + ",\n" + message + "\nBest regards"
	            );
	        }
	    }
	   }
