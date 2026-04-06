package service;
import java.util.ArrayList;

import domain.user;
/**
 * Observer interface for sending notifications.
 * @author sabre
 */
public interface NotificationObserver {

	    void update(ArrayList<user> users, String message);
	}
	
