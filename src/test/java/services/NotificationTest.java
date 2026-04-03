package services;
import domain.*;
import service.*;

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

	        // Mockito creates a fake NotificationObserver
	        mockObserver = Mockito.mock(NotificationObserver.class);
	        appointmentService.addObserver(mockObserver);

	        testUser = new user("U1", "Sabreen", "sabreen@gmail.com", "1234");
	        testProperty = new property("P1", "Car", "Nablus", 25000, 10);
	        testTime = new time();
	        testTime.setdate(10, 30);
	    }
	    
	    @Test
	    void testNotificationSentOnBooking() {
	        appointmentService.bookAppointment("A1", testUser, testProperty, testTime);

	        // Verify update() was called once with any message
	        verify(mockObserver, times(1)).update(eq(testUser), anyString());
	    }

	    @Test
	    void testNotificationSentOnCancellation() {
	        appointmentService.bookAppointment("A1", testUser, testProperty, testTime);
	        appointmentService.cancelAppointment("A1");

	        // Verify update() was called twice (once for booking, once for cancellation)
	        verify(mockObserver, times(2)).update(eq(testUser), anyString());
	    }

	    @Test
	    void testReminderNotification() {
	        appointmentService.bookAppointment("A1", testUser, testProperty, testTime);
	        appointmentService.sendReminder("A1");

	        // Verify update() was called twice (booking + reminder)
	        verify(mockObserver, times(2)).update(eq(testUser), anyString());
	    }

	    @Test
	    void testNoNotificationForInvalidAppointment() {
	        // Try to cancel non-existent appointment
	        appointmentService.cancelAppointment("INVALID");

	        // Verify no notification was sent
	        verify(mockObserver, never()).update(any(), anyString());
	    }

	    @Test
	    void testObserverCanBeRemoved() {
	        appointmentService.removeObserver(mockObserver);
	        appointmentService.bookAppointment("A1", testUser, testProperty, testTime);

	        // Verify no notification sent after observer removed
	        verify(mockObserver, never()).update(any(), anyString());
	    }
}
