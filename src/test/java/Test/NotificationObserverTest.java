package Test;
import service.*;
import domain.user;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NotificationObserverTest {

    // implementation مبسطة للاختبار
    static class SimpleObserver implements NotificationObserver {
        boolean called = false;
        ArrayList<user> receivedUsers;
        String receivedMessage;

        @Override
        public void update(ArrayList<user> users, String message) {
            called           = true;
            receivedUsers    = users;
            receivedMessage  = message;
        }
    }

    @Test
    void testObserverIsCalledOnUpdate() {
        SimpleObserver obs = new SimpleObserver();
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        obs.update(users, "Test message");

        assertTrue(obs.called);
    }

    @Test
    void testObserverReceivesCorrectMessage() {
        SimpleObserver obs = new SimpleObserver();
        obs.update(new ArrayList<>(), "Hello Observer");
        assertEquals("Hello Observer", obs.receivedMessage);
    }

    @Test
    void testObserverReceivesCorrectUsers() {
        SimpleObserver obs = new SimpleObserver();
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        obs.update(users, "msg");

        assertEquals(1, obs.receivedUsers.size());
        assertEquals("Ahmad", obs.receivedUsers.get(0).getName());
    }

    @Test
    void testConsoleNotificationImplementsObserver() {
        assertTrue(new ConsoleNotification() instanceof NotificationObserver);
    }

    @Test
    void testMockNotificationImplementsObserver() {
        assertTrue(new MockNotification() instanceof NotificationObserver);
    }

    @Test
    void testEmailNotificationImplementsObserver() {
        EmailService es = new EmailService("a@a.com", "pass") {
            @Override public void sendEmail(String t, String s, String b) {}
        };
        assertTrue(new EmailNotification(es) instanceof NotificationObserver);
    }
}