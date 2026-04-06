package service;
import domain.user;
import java.util.ArrayList;


public class ConsoleNotification implements NotificationObserver{

	 @Override
	    public void update(ArrayList<user> users, String message) {
	        for (user u : users) {
	            System.out.println("[NOTIFICATION] To: " + u.getName()
	                    + " | " + message);
	        }
	    }
	}