package Test;
import domain.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class mangfileTest {


    @Test
    void testSaveAndLoadCustomers() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass1234"));
        users.add(new user("USR002", "Sara",  "sara@email.com",  "abc123"));

        mangfile.saveToFile(mangfile.FileType.CUSTOMER, users);

        ArrayList<user> loaded = mangfile.loadFromFile(mangfile.FileType.CUSTOMER);

        assertEquals(2, loaded.size());
        assertEquals("USR001", loaded.get(0).getId());
        assertEquals("Sara",   loaded.get(1).getName());
    }

    @Test
    void testSaveAndLoadProperties() {
        user owner = new user("USR001", "Ahmad", "ahmad@email.com", "pass1234");
        ArrayList<property> props = new ArrayList<>();
        props.add(new property("P001", "Villa", "Ramallah", 200000.0, 5, owner));

        mangfile.saveToFile(mangfile.FileType.PROPERTY, props);

        ArrayList<property> loaded = mangfile.loadFromFile(mangfile.FileType.PROPERTY);

        assertEquals(1, loaded.size());
        assertEquals("P001",  loaded.get(0).getPropertyId());
        assertEquals("Villa", loaded.get(0).getName());
    }

    @Test
    void testSaveAndLoadAppointments() {
        user owner = new user("USR001", "Ahmad", "ahmad@email.com", "pass1234");
        property prop = new property("P001", "Villa", "Ramallah", 200000.0, 5, owner);

        time t = new time();
        java.time.LocalDateTime future = java.time.LocalDateTime.now().plusDays(3);
        t.setdate(10, 0, future.getDayOfMonth(), future.getMonthValue(), future.getYear());
        t.setenddate(30);

        ArrayList<appointment> appts = new ArrayList<>();
        appts.add(new appointment("APT001", prop, t, appointment.AppointmentType.IN_PERSON));

        mangfile.saveToFile(mangfile.FileType.APPOINTMENT, appts);

        ArrayList<appointment> loaded = mangfile.loadFromFile(mangfile.FileType.APPOINTMENT);

        assertEquals(1, loaded.size());
        assertEquals("APT001", loaded.get(0).getAppointmentId());
    }

    @Test
    void testLoadFromNonExistentFileReturnsEmptyList() {
        // نحذف الملف إذا كان موجوداً
        File f = new File(mangfile.FileType.BOOKING.getPath());
        if (f.exists()) f.delete();

        ArrayList<Object> result = mangfile.loadFromFile(mangfile.FileType.BOOKING);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveEmptyListAndLoad() {
        ArrayList<user> empty = new ArrayList<>();
        mangfile.saveToFile(mangfile.FileType.CUSTOMER, empty);

        ArrayList<user> loaded = mangfile.loadFromFile(mangfile.FileType.CUSTOMER);
        assertTrue(loaded.isEmpty());
    }
}
