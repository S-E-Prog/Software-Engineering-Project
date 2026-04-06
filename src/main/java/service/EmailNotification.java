package service;
import java.util.ArrayList;

import domain.user;

public class EmailNotification implements NotificationObserver{

	   @Override
	    public void update(ArrayList<user> users, String message) {
	        // In real life this would send an actual email
		   for (user u : users) {
	            System.out.println("[EMAIL] To: " + u.getEmail()
	                    + " | Message: " + message);
	        }
		   }
	   }
