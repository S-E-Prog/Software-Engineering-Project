package service;
import java.util.ArrayList;

import domain.user;
public interface NotificationObserver {

	    void update(ArrayList<user> users, String message);
	}
	
