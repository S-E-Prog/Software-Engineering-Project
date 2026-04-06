package services;
import domain.*;
import service.*;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class NotificationTest {

	  private AppointmentService appointmentService;
	    private NotificationObserver mockObserver;
	    private user testUser;
	    private property testProperty;
	    private time testTime;

	    @BeforeEach
	    void setUp() {
	        appointmentService = new AppointmentService();
	        mockObserver = Mockito.mock(NotificationObserver.class);
	        appointmentService.addObserver(mockObserver);

	        testUser = new user("U1", "Sabreen", "sabreen@gmail.com", "1234");
	        testProperty = new property("P1", "Nablus", "Nice apartment", 500.0, 5,testUser);
	        testTime = new time();
	        testTime.setdate(10, 30);
	    }

	    @Test
	    void testNotificationSentOnBooking() {
	        appointmentService.bookAppointment("A1", testUser, testProperty, testTime);

	        verify(mockObserver, times(1))
	                .update(any(ArrayList.class), anyString());
	    }

	    @Test
	    void testNotificationSentOnCancellation() {
	        appointmentService.bookAppointment("A1", testUser, testProperty, testTime);
	        appointmentService.cancelAppointment("A1");

	        verify(mockObserver, times(2))
	                .update(any(ArrayList.class), anyString());
	    }

	    @Test
	    void testReminderNotification() {
	        appointmentService.bookAppointment("A1", testUser, testProperty, testTime);
	        appointmentService.sendReminder("A1");

	        verify(mockObserver, times(2))
	                .update(any(ArrayList.class), anyString());
	    }

	    @Test
	    void testNoNotificationForInvalidAppointment() {
	        appointmentService.cancelAppointment("INVALID");

	        verify(mockObserver, never())
	                .update(any(ArrayList.class), anyString());
	    }

	    @Test
	    void testObserverCanBeRemoved() {
	        appointmentService.removeObserver(mockObserver);
	        appointmentService.bookAppointment("A1", testUser, testProperty, testTime);

	        verify(mockObserver, never())
	                .update(any(ArrayList.class), anyString());
	    }
}
