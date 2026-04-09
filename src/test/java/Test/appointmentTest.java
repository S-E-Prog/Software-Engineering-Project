package Test;
import domain.*;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class appointmentTest {

    private appointment appt;
    private user u1, u2;
    private property prop;
    private time futureTime;

    @BeforeEach
    void setUp() {
        u1   = new user("USR001", "Ahmad", "ahmad@email.com", "pass1234");
        u2   = new user("USR002", "Sara",  "sara@email.com",  "abc123");
        prop = new property("P001", "Sunset Villa", "Ramallah St 5", 250000.0, 5, u1);

        futureTime = new time();
        LocalDateTime future = LocalDateTime.now().plusDays(3);
        futureTime.setdate(10, 0,
                future.getDayOfMonth(),
                future.getMonthValue(),
                future.getYear());
        futureTime.setenddate(30);

        appt = new appointment("APT001", prop, futureTime, appointment.AppointmentType.IN_PERSON);
    }

    // ── الحالة الأولية ──────────────────────────────────────────────

    @Test
    void testInitialStatusIsAvailable() {
        assertEquals(appointment.AppointmentStatus.AVAILABLE, appt.getStatus());
    }

    @Test
    void testGetAppointmentId() {
        assertEquals("APT001", appt.getAppointmentId());
    }

    @Test
    void testGetProperty() {
        assertEquals("P001", appt.getProperty().getPropertyId());
    }

    @Test
    void testInitialBookingCountIsZero() {
        assertEquals(0, appt.getBookingCount());
    }

    // ── الحجز ───────────────────────────────────────────────────────

    @Test
    void testAddBookingSuccess() {
        assertTrue(appt.addBooking(u1));
        assertEquals(1, appt.getBookingCount());
    }

    @Test
    void testAddBookingDuplicateReturnsFalse() {
        appt.addBooking(u1);
        assertFalse(appt.addBooking(u1));
        assertEquals(1, appt.getBookingCount());
    }

    @Test
    void testIsBookedByReturnsTrueAfterBooking() {
        appt.addBooking(u1);
        assertTrue(appt.isBookedBy(u1));
    }

    @Test
    void testIsBookedByReturnsFalseForOtherUser() {
        appt.addBooking(u1);
        assertFalse(appt.isBookedBy(u2));
    }

    @Test
    void testRemoveBooking() {
        appt.addBooking(u1);
        assertTrue(appt.removeBooking(u1));
        assertFalse(appt.isBookedBy(u1));
    }

    @Test
    void testRemoveNonExistentBookingReturnsFalse() {
        assertFalse(appt.removeBooking(u2));
    }

    @Test
    void testAddBookingBeyondCapacityFails() {
        // capacity = min(type.maxParticipants=5, prop.maxViewingCapacity=5) = 5
        // IN_PERSON maxParticipants = 5
        for (int i = 0; i < 5; i++) {
            user extra = new user("USR00" + (i + 3), "User" + i, "u" + i + "@e.com", "pass");
            appt.addBooking(extra);
        }
        user overflow = new user("USR099", "Extra", "extra@e.com", "pass");
        assertFalse(appt.addBooking(overflow));
    }

    // ── الحالات ─────────────────────────────────────────────────────

    @Test
    void testConfirm() {
        appt.confirm();
        assertTrue(appt.isConfirmed());
        assertEquals(appointment.AppointmentStatus.CONFIRMED, appt.getStatus());
    }

    @Test
    void testCancel() {
        appt.cancel();
        assertEquals(appointment.AppointmentStatus.CANCELLED, appt.getStatus());
    }

    @Test
    void testComplete() {
        appt.complete();
        assertEquals(appointment.AppointmentStatus.COMPLETED, appt.getStatus());
    }

    @Test
    void testIsAvailableAfterCreation() {
        assertTrue(appt.isAvailable());
    }

    @Test
    void testIsAvailableFalseAfterCancel() {
        appt.cancel();
        assertFalse(appt.isAvailable());
    }

    // ── التحقق ──────────────────────────────────────────────────────

    @Test
    void testValidateDurationValid() {
        assertNull(appt.validateDuration(30)); // IN_PERSON max = 45
    }

    @Test
    void testValidateDurationTooLong() {
        assertNotNull(appt.validateDuration(60)); // فوق الـ 45
    }

    @Test
    void testValidateDurationZero() {
        assertNotNull(appt.validateDuration(0));
    }

    @Test
    void testValidateBookingAllowedWhenNotFull() {
        assertNull(appt.validateBookingAllowed());
    }

    @Test
    void testValidateBookingAllowedWhenFull() {
        for (int i = 0; i < 5; i++) {
            user extra = new user("USR00" + (i + 3), "User" + i, "u" + i + "@e.com", "pass");
            appt.addBooking(extra);
        }
        assertNotNull(appt.validateBookingAllowed());
    }

    @Test
    void testValidateAppointmentTimePast() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        assertNotNull(appointment.validateAppointmentTime(past, appointment.AppointmentType.IN_PERSON));
    }

    @Test
    void testValidateAppointmentTimeFuture() {
        LocalDateTime future = LocalDateTime.now().plusDays(2);
        assertNull(appointment.validateAppointmentTime(future, appointment.AppointmentType.IN_PERSON));
    }

    @Test
    void testValidateUrgentAppointmentTooFar() {
        LocalDateTime tooFar = LocalDateTime.now().plusWeeks(2);
        assertNotNull(appointment.validateAppointmentTime(tooFar, appointment.AppointmentType.URGENT));
    }

    @Test
    void testValidateUrgentAppointmentWithinWeek() {
        LocalDateTime withinWeek = LocalDateTime.now().plusDays(3);
        assertNull(appointment.validateAppointmentTime(withinWeek, appointment.AppointmentType.URGENT));
    }

    // ── AppointmentType ─────────────────────────────────────────────

    @Test
    void testAppointmentTypeDisplayName() {
        assertEquals("In-Person", appointment.AppointmentType.IN_PERSON.getDisplayName());
    }

    @Test
    void testUrgentMaxParticipants() {
        assertEquals(1, appointment.AppointmentType.URGENT.getMaxParticipants());
    }

    @Test
    void testGroupCanBeVirtual() {
        assertTrue(appointment.AppointmentType.GROUP.canBeVirtual());
    }

    @Test
    void testInPersonCannotBeVirtual() {
        assertFalse(appointment.AppointmentType.IN_PERSON.canBeVirtual());
    }

    @Test
    void testGetEffectiveMaxParticipants() {
        // IN_PERSON maxParticipants=5, prop capacity=5 → min=5
        assertEquals(5, appt.getEffectiveMaxParticipants());
    }

    // ── toString & time ─────────────────────────────────────────────

    @Test
    void testToStringContainsId() {
        assertTrue(appt.toString().contains("APT001"));
    }

    @Test
    void testGetAppointmentTimeString() {
        assertFalse(appt.getAppointmentTimeString().isEmpty());
    }

    @Test
    void testIsExpiredFalseForFutureAppointment() {
        assertFalse(appt.isExpired());
    }
}