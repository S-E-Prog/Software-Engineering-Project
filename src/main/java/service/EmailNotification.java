package service;
import java.util.ArrayList;

import domain.user;

/**
 * Handles email notifications to users.
 * @author sabre
 * 
 */
public class EmailNotification implements NotificationObserver{

	   /**
	 * Sends an email notification to the user.
	 * @param users the list of users foe a specific appointment.
	 * @param message the notification content.
	 */
	@Override
	    public void update(ArrayList<user> users, String message) {
	        // In real life this would send an actual email
		   for (user u : users) {
	            System.out.println("[EMAIL] To: " + u.getEmail()
	                    + " | Message: " + message);
	        }
		   }
	   }
