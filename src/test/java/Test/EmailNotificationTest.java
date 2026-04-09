package Test;
import service.*;
import domain.user;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmailNotificationTest {

    // EmailService وهمي يحفظ الإيميلات بدل ما يبعثها فعلاً
    static class FakeEmailService extends EmailService {
        List<String> sentTo      = new ArrayList<>();
        List<String> sentSubject = new ArrayList<>();
        List<String> sentBody    = new ArrayList<>();

        FakeEmailService() {
            super("fake@email.com", "fakepass");
        }

        @Override
        public void sendEmail(String toEmail, String subject, String body) {
            sentTo.add(toEmail);
            sentSubject.add(subject);
            sentBody.add(body);
        }
    }

    private FakeEmailService fakeService;
    private EmailNotification emailNotification;

    @BeforeEach
    void setUp() {
        fakeService       = new FakeEmailService();
        emailNotification = new EmailNotification(fakeService);
    }

    @Test
    void testUpdateSendsEmailToSingleUser() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        emailNotification.update(users, "Your appointment is confirmed.");

        assertEquals(1, fakeService.sentTo.size());
        assertEquals("ahmad@email.com", fakeService.sentTo.get(0));
    }

    @Test
    void testUpdateSendsEmailToAllUsers() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));
        users.add(new user("USR002", "Sara",  "sara@email.com",  "pass"));

        emailNotification.update(users, "Reminder");

        assertEquals(2, fakeService.sentTo.size());
        assertTrue(fakeService.sentTo.contains("ahmad@email.com"));
        assertTrue(fakeService.sentTo.contains("sara@email.com"));
    }

    @Test
    void testUpdateUsesCorrectSubject() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        emailNotification.update(users, "Some message");

        assertEquals("Appointment Notification", fakeService.sentSubject.get(0));
    }

    @Test
    void testUpdateBodyContainsUserName() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        emailNotification.update(users, "Your slot is ready.");

        assertTrue(fakeService.sentBody.get(0).contains("Ahmad"));
    }

    @Test
    void testUpdateBodyContainsMessage() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        emailNotification.update(users, "Your slot is ready.");

        assertTrue(fakeService.sentBody.get(0).contains("Your slot is ready."));
    }

    @Test
    void testUpdateBodyContainsDearPrefix() {
        ArrayList<user> users = new ArrayList<>();
        users.add(new user("USR001", "Ahmad", "ahmad@email.com", "pass"));

        emailNotification.update(users, "msg");

        assertTrue(fakeService.sentBody.get(0).contains("Dear Ahmad"));
    }

    @Test
    void testUpdateWithEmptyListSendsNoEmail() {
        emailNotification.update(new ArrayList<>(), "Hello");
        assertTrue(fakeService.sentTo.isEmpty());
    }

    @Test
    void testUpdateImplementsNotificationObserver() {
        assertTrue(emailNotification instanceof NotificationObserver);
    }
}