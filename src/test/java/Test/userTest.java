package Test;
import domain.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class userTest {

    private user u;

    @BeforeEach
    void setUp() {
        u = new user("USR001", "Ahmad", "ahmad@email.com", "pass1234");
    }

    @Test
    void testGetId() {
        assertEquals("USR001", u.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Ahmad", u.getName());
    }

    @Test
    void testSetName() {
        u.setName("Salem");
        assertEquals("Salem", u.getName());
    }

    @Test
    void testGetEmail() {
        assertEquals("ahmad@email.com", u.getEmail());
    }

    @Test
    void testSetEmail() {
        u.setEmail("salem@email.com");
        assertEquals("salem@email.com", u.getEmail());
    }

    @Test
    void testGetPassword() {
        assertEquals("pass1234", u.getPassword());
    }

    @Test
    void testSetPassword() {
        u.setPassword("newpass99");
        assertEquals("newpass99", u.getPassword());
    }

    @Test
    void testToStringContainsIdAndName() {
        String result = u.toString();
        assertTrue(result.contains("USR001"));
        assertTrue(result.contains("Ahmad"));
    }
}