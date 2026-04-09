package Test;
import domain.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class propertyTest {

    private user owner;
    private property prop;

    @BeforeEach
    void setUp() {
        owner = new user("USR001", "Ahmad", "ahmad@email.com", "pass1234");
        prop  = new property("P001", "Sunset Villa", "Ramallah St 5", 250000.0, 10, owner);
    }

    @Test
    void testGetPropertyId() {
        assertEquals("P001", prop.getPropertyId());
    }

    @Test
    void testGetName() {
        assertEquals("Sunset Villa", prop.getName());
    }

    @Test
    void testSetName() {
        prop.setName("Ocean View");
        assertEquals("Ocean View", prop.getName());
    }

    @Test
    void testGetAddress() {
        assertEquals("Ramallah St 5", prop.getAddress());
    }

    @Test
    void testSetAddress() {
        prop.setAddress("Nablus Rd 12");
        assertEquals("Nablus Rd 12", prop.getAddress());
    }

    @Test
    void testGetPrice() {
        assertEquals(250000.0, prop.getPrice());
    }

    @Test
    void testSetPrice() {
        prop.setPrice(300000.0);
        assertEquals(300000.0, prop.getPrice());
    }

    @Test
    void testGetMaxViewingCapacity() {
        assertEquals(10, prop.getMaxViewingCapacity());
    }

    @Test
    void testSetMaxViewingCapacity() {
        prop.setMaxViewingCapacity(5);
        assertEquals(5, prop.getMaxViewingCapacity());
    }

    @Test
    void testGetOwner() {
        assertEquals("USR001", prop.getOwner().getId());
    }

    @Test
    void testSetOwner() {
        user newOwner = new user("USR002", "Sara", "sara@email.com", "abc123");
        prop.setOwner(newOwner);
        assertEquals("USR002", prop.getOwner().getId());
    }

    @Test
    void testOwnerCanBeNull() {
        property p = new property("P002", "Test", "Addr", 100.0, 5, null);
        assertNull(p.getOwner());
    }
}