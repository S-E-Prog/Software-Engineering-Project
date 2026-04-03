package service;
import domain.user;

public class ConsoleNotification implements NotificationObserver{

	
	  @Override
	    public void update(user user, String message) {
	        System.out.println("[NOTIFICATION] To: " + user.getName() + " | " + message);
	    }
}
