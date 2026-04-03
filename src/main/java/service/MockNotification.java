package service;
import domain.user;
import java.util.ArrayList;
import java.util.List;

public class MockNotification implements NotificationObserver{

	 private List<String> sentMessages = new ArrayList<>();
	 
	 
	 @Override
	    public void update(user user, String message) {
	        sentMessages.add("To: " + user.getName() + " | " + message);
	    }
	 
	 public List<String> getSentMessages() {
	        return sentMessages;
	    }
	 
	   public void clear() {
	        sentMessages.clear();
	    }
}
