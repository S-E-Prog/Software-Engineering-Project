package Test;
import service.*;
import domain.user;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MockNotificationTest {

    private MockNotification mock;

    @BeforeEach
    void setUp() {
        mock = new MockNotification();
    }

    @Test
    void testInitiallyEmpty() {
        assertTrue(mock.getSentMessages().isEmpty());
    }

    @Test
    void testUpdateStoresMessageForSingleUser() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        mock.update(users, "Appointment confirmed.");

        assertEquals(1, mock.getSentMessages().size());
        assertTrue(mock.getSentMessages().get(0).contains("Ahmad"));
        assertTrue(mock.getSentMessages().get(0).contains("Appointment confirmed."));
    }

    @Test
    void testUpdateStoresOneMessagePerUser() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));
        users.add(new user("USR002", "Sara",  "sara@email.com",  "pass"));

        mock.update(users, "Hello");

        assertEquals(2, mock.getSentMessages().size());
    }

    @Test
    void testUpdateWithEmptyListStoresNothing() {
        mock.update(new ArrayList<>(), "Hello");
        assertTrue(mock.getSentMessages().isEmpty());
    }

    @Test
    void testMultipleUpdateCallsAccumulate() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        mock.update(users, "First message");
        mock.update(users, "Second message");

        assertEquals(2, mock.getSentMessages().size());
    }

    @Test
    void testMessageFormatContainsToPrefix() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        mock.update(users, "Test");

        assertTrue(mock.getSentMessages().get(0).startsWith("To: Ahmad"));
    }

    @Test
    void testClearEmptiesMessages() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));
        mock.update(users, "msg");

        mock.clear();

        assertTrue(mock.getSentMessages().isEmpty());
    }

    @Test
    void testClearThenUpdateWorksNormally() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));
        mock.update(users, "first");
        mock.clear();
        mock.update(users, "second");

        assertEquals(1, mock.getSentMessages().size());
        assertTrue(mock.getSentMessages().get(0).contains("second"));
    }

    @Test
    void testMessagesListIsNotSharedBetweenInstances() {
        MockNotification mock2 = new MockNotification();
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        mock.update(users, "msg");

        assertTrue(mock2.getSentMessages().isEmpty());
    }
}