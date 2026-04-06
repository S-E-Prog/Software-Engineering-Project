package service;
import domain.user;
import java.util.ArrayList;
import java.util.List;

public class MockNotification implements NotificationObserver{

	 private List<String> sentMessages = new ArrayList<>();
	 
	 
	 @Override
	 public void update(ArrayList<user> users, String message) {
	        for (user u : users) {
	            sentMessages.add("To: " + u.getName() + " | " + message);
	        }
	    }
	 
	 public List<String> getSentMessages() {
	        return sentMessages;
	    }
	 
	   public void clear() {
	        sentMessages.clear();
	    }
}
