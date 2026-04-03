package service;
import domain.user;

public class EmailNotification implements NotificationObserver{

	   @Override
	    public void update(user user, String message) {
	        // In real life this would send an actual email
	        System.out.println("[EMAIL] To: " + user.getEmail() 
	                         + " | Message: " + message);
	    }
}
