package Test;

import domain.*;
import org.junit.jupiter.api.*;
import service.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {

    private AppointmentService service;
    private user u1, u2;
    private property prop1, prop2;
    private time futureTime1, futureTime2;

    static class TestObserver implements NotificationObserver {
        List<String> messages = new ArrayList<>();
        @Override
        public void update(ArrayList<user> users, String message) {
            messages.add(message);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        service = new AppointmentService();

        // نفرّغ القوائم حتى لا تتداخل بيانات الـ disk بين التستات
        setField(service, "appointmentsList", new ArrayList<>());
        setField(service, "customList",       new ArrayList<>());
        setField(service, "propertiesList",   new ArrayList<>());

        u1    = new user("USR001", "Ahmad", "ahmad@email.com", "pass1234");
        u2    = new user("USR002", "Sara",  "sara@email.com",  "abc123");
        prop1 = new property("P001", "Sunset Villa", "Ramallah St 5", 250000.0, 5, u1);
        prop2 = new property("P002", "Ocean View",   "Nablus Rd 12",  300000.0, 3, u2);

        futureTime1 = makeTime(LocalDateTime.now().plusDays(3), 30);
        futureTime2 = makeTime(LocalDateTime.now().plusDays(5), 45);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    private time makeTime(LocalDateTime dt, int durationMin) {
        time t = new time();
        t.setdate(dt.getHour(), dt.getMinute(),
                dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear());
        t.setenddate(durationMin);
        return t;
    }

    // ================================================================
    //  bookAppointment
    // ================================================================

    @Test
    void testBookAppointmentSuccess() {
        appointment a = service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertNotNull(a);
        assertEquals("APT001", a.getAppointmentId());
        assertTrue(a.isBookedBy(u1));
    }

    @Test
    void testBookAppointmentAddedToList() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertEquals(1, service.getAllAppointments().size());
    }

    @Test
    void testBookAppointmentDuplicateSlotReturnNull() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        appointment duplicate = service.bookAppointment("APT002", u2, prop1, futureTime1);
        assertNull(duplicate);
        assertEquals(1, service.getAllAppointments().size());
    }

    @Test
    void testBookAppointmentDifferentPropertySameTimeAllowed() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        appointment a2 = service.bookAppointment("APT002", u2, prop2, futureTime1);
        assertNotNull(a2);
        assertEquals(2, service.getAllAppointments().size());
    }

    @Test
    void testBookAppointmentSamePropertyDifferentTimeAllowed() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        appointment a2 = service.bookAppointment("APT002", u2, prop1, futureTime2);
        assertNotNull(a2);
    }

    @Test
    void testBookAppointmentNotifiesObserver() {
        TestObserver obs = new TestObserver();
        service.addObserver(obs);
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertEquals(1, obs.messages.size());
        assertTrue(obs.messages.get(0).contains("confirmed"));
    }

    // ================================================================
    //  cancelAppointment
    // ================================================================

    @Test
    void testCancelAppointmentSuccess() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertTrue(service.cancelAppointment("APT001"));
        assertEquals(appointment.AppointmentStatus.CANCELLED,
                service.getAllAppointments().get(0).getStatus());
    }

    @Test
    void testCancelAppointmentNotFound() {
        assertFalse(service.cancelAppointment("NOTEXIST"));
    }

    @Test
    void testCancelAlreadyCancelledReturnsFalse() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.cancelAppointment("APT001");
        assertFalse(service.cancelAppointment("APT001"));
    }

    @Test
    void testCancelNotifiesObserver() {
        TestObserver obs = new TestObserver();
        service.addObserver(obs);
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.cancelAppointment("APT001");
        assertEquals(2, obs.messages.size());
        assertTrue(obs.messages.get(1).contains("cancelled"));
    }

    // ================================================================
    //  adminCancelAppointment
    // ================================================================

    @Test
    void testAdminCancelSuccess() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        user admin = new user("ADM001", "Admin", "admin@email.com", "admin123");
        assertTrue(service.adminCancelAppointment("APT001", admin));
    }

    @Test
    void testAdminCancelNotFound() {
        user admin = new user("ADM001", "Admin", "admin@email.com", "admin123");
        assertFalse(service.adminCancelAppointment("NOTEXIST", admin));
    }

    @Test
    void testAdminCancelAlreadyCancelledReturnsFalse() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        user admin = new user("ADM001", "Admin", "admin@email.com", "admin123");
        service.cancelAppointment("APT001");
        assertFalse(service.adminCancelAppointment("APT001", admin));
    }

    @Test
    void testAdminCancelNotifiesObserverWithAdminMessage() {
        TestObserver obs = new TestObserver();
        service.addObserver(obs);
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        user admin = new user("ADM001", "Admin", "admin@email.com", "admin123");
        service.adminCancelAppointment("APT001", admin);
        assertEquals(2, obs.messages.size());
        assertTrue(obs.messages.get(1).contains("cancelled"));
    }

    // ================================================================
    //  isSlotAvailable
    // ================================================================

    @Test
    void testIsSlotAvailableTrue() {
        assertTrue(service.isSlotAvailable(prop1, futureTime1));
    }

    @Test
    void testIsSlotAvailableFalseAfterBooking() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertFalse(service.isSlotAvailable(prop1, futureTime1));
    }

    @Test
    void testIsSlotAvailableDifferentPropertyStillAvailable() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertTrue(service.isSlotAvailable(prop2, futureTime1));
    }

    @Test
    void testIsSlotAvailableAfterCancelBecomesAvailableAgain() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.cancelAppointment("APT001");
        assertTrue(service.isSlotAvailable(prop1, futureTime1));
    }

    // ================================================================
    //  getAvailableSlots
    // ================================================================

    @Test
    void testGetAvailableSlotsAllFree() {
        List<time> allSlots = List.of(futureTime1, futureTime2);
        List<time> available = service.getAvailableSlots(prop1, allSlots);
        assertEquals(2, available.size());
    }

    @Test
    void testGetAvailableSlotsOneBooked() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        List<time> allSlots = List.of(futureTime1, futureTime2);
        List<time> available = service.getAvailableSlots(prop1, allSlots);
        assertEquals(1, available.size());
        assertTrue(available.contains(futureTime2));
    }

    @Test
    void testGetAvailableSlotsEmptyWhenAllBooked() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.bookAppointment("APT002", u2, prop1, futureTime2);
        List<time> allSlots = List.of(futureTime1, futureTime2);
        List<time> available = service.getAvailableSlots(prop1, allSlots);
        assertTrue(available.isEmpty());
    }

    // ================================================================
    //  modifyAppointment
    // ================================================================

    @Test
    void testModifyAppointmentSuccess() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertTrue(service.modifyAppointment("APT001", futureTime2));
    }

    @Test
    void testModifyAppointmentNotFound() {
        assertFalse(service.modifyAppointment("NOTEXIST", futureTime2));
    }

    @Test
    void testModifyAppointmentCancelledReturnsFalse() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.cancelAppointment("APT001");
        assertFalse(service.modifyAppointment("APT001", futureTime2));
    }

    @Test
    void testModifyAppointmentNotifiesObserver() {
        TestObserver obs = new TestObserver();
        service.addObserver(obs);
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.modifyAppointment("APT001", futureTime2);
        assertEquals(2, obs.messages.size());
        assertTrue(obs.messages.get(1).contains("modified"));
    }

    // ================================================================
    //  getAppointmentsByProperty
    // ================================================================

    @Test
    void testGetAppointmentsByPropertyReturnsCorrect() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.bookAppointment("APT002", u2, prop2, futureTime2);
        List<appointment> result = service.getAllAppointments().stream()
                .filter(a -> a.getProperty().getPropertyId().equals(prop1.getPropertyId()))
                .toList();
        assertEquals(1, result.size());
        assertEquals("APT001", result.get(0).getAppointmentId());
    }

    @Test
    void testGetAppointmentsByPropertyEmptyWhenNone() {
        List<appointment> result = service.getAllAppointments().stream()
                .filter(a -> a.getProperty().getPropertyId().equals(prop1.getPropertyId()))
                .toList();
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAppointmentsByPropertyMultiple() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.bookAppointment("APT002", u2, prop1, futureTime2);
        List<appointment> result = service.getAllAppointments().stream()
                .filter(a -> a.getProperty().getPropertyId().equals(prop1.getPropertyId()))
                .toList();
        assertEquals(2, result.size());
    }

    // ================================================================
    //  getAppointmentsByUser
    // ================================================================

    @Test
    void testGetAppointmentsByUserReturnsCorrect() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.bookAppointment("APT002", u2, prop2, futureTime2);
        List<appointment> result = service.getBookedByUser(u1);
        assertEquals(1, result.size());
        assertEquals("APT001", result.get(0).getAppointmentId());
    }

    @Test
    void testGetAppointmentsByUserEmptyWhenNone() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        List<appointment> result = service.getBookedByUser(u2);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAppointmentsByUserMultipleBookings() {
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        service.bookAppointment("APT002", u1, prop2, futureTime2);
        assertEquals(2, service.getBookedByUser(u1).size());
    }

    // ================================================================
    //  Observer — addObserver / removeObserver
    // ================================================================

    @Test
    void testRemoveObserverStopsNotifications() {
        TestObserver obs = new TestObserver();
        service.addObserver(obs);
        service.removeObserver(obs);
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertTrue(obs.messages.isEmpty());
    }

    @Test
    void testMultipleObserversAllNotified() {
        TestObserver obs1 = new TestObserver();
        TestObserver obs2 = new TestObserver();
        service.addObserver(obs1);
        service.addObserver(obs2);
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertEquals(1, obs1.messages.size());
        assertEquals(1, obs2.messages.size());
    }

    // ================================================================
    //  sendReminder
    // ================================================================

    @Test
    void testSendReminderForConfirmedAppointment() {
        TestObserver obs = new TestObserver();
        service.addObserver(obs);
        appointment a = service.bookAppointment("APT001", u1, prop1, futureTime1);
        assertNotNull(a, "bookAppointment must return a non-null appointment");
        a.confirm();
        service.sendReminder("APT001");
        assertTrue(obs.messages.stream().anyMatch(m -> m.contains("Reminder")));
    }

    @Test
    void testSendReminderForAvailableAppointmentDoesNotNotify() {
        TestObserver obs = new TestObserver();
        service.addObserver(obs);
        service.bookAppointment("APT001", u1, prop1, futureTime1);
        int msgsBefore = obs.messages.size();
        service.sendReminder("APT001");
        assertEquals(msgsBefore, obs.messages.size());
    }

    @Test
    void testSendReminderNotFoundDoesNothing() {
        TestObserver obs = new TestObserver();
        service.addObserver(obs);
        service.sendReminder("NOTEXIST");
        assertTrue(obs.messages.isEmpty());
    }
}