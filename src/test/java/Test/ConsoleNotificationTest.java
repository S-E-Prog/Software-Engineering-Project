package Test;

import domain.user;
import service.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleNotificationTest {

    private ConsoleNotification notification;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        notification = new ConsoleNotification();
        // نلتقط System.out لنتحقق من الطباعة
        outContent  = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testUpdatePrintsMessageForSingleUser() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        notification.update(users, "Your appointment is confirmed.");

        String output = outContent.toString();
        assertTrue(output.contains("Ahmad"));
        assertTrue(output.contains("Your appointment is confirmed."));
        assertTrue(output.contains("[NOTIFICATION]"));
    }

    @Test
    void testUpdatePrintsMessageForMultipleUsers() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));
        users.add(new user("USR002", "Sara",  "sara@email.com",  "pass"));

        notification.update(users, "Reminder: appointment tomorrow.");

        String output = outContent.toString();
        assertTrue(output.contains("Ahmad"));
        assertTrue(output.contains("Sara"));
    }

    @Test
    void testUpdateWithEmptyListPrintsNothing() {
        notification.update(new ArrayList<>(), "Hello!");
        assertEquals("", outContent.toString());
    }

    @Test
    void testUpdatePrintsCorrectFormat() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        notification.update(users, "Test message");

        String output = outContent.toString();
        assertTrue(output.contains("[NOTIFICATION] To: Ahmad"));
        assertTrue(output.contains("Test message"));
    }

    @Test
    void testUpdatePrintsOneLinePerUser() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));
        users.add(new user("USR002", "Sara",  "sara@email.com",  "pass"));
        users.add(new user("USR003", "Omar",  "omar@email.com",  "pass"));

        notification.update(users, "msg");

        long lines = outContent.toString().lines()
                .filter(l -> l.contains("[NOTIFICATION]"))
                .count();
        assertEquals(3, lines);
    }
}