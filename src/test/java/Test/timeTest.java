package Test;

import domain.*;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class timeTest {

    private time t;

    @BeforeEach
    void setUp() {
        t = new time();
    }

    @Test
    void testSetdateAndGetdatetime() {
        // تاريخ في المستقبل
        LocalDateTime future = LocalDateTime.now().plusDays(2);
        t.setdate(10, 30,
                future.getDayOfMonth(),
                future.getMonthValue(),
                future.getYear());

        LocalDateTime result = t.getdatetime();
        assertEquals(10, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    void testSetenddateAddsMinutes() {
        LocalDateTime future = LocalDateTime.now().plusDays(2);
        t.setdate(10, 0,
                future.getDayOfMonth(),
                future.getMonthValue(),
                future.getYear());
        t.setenddate(30);

        LocalDateTime end = t.getendDateTime();
        assertEquals(30, end.getMinute());
    }

    @Test
    void testIsendReturnsFalseForFutureDate() {
        LocalDateTime future = LocalDateTime.now().plusDays(5);
        t.setdate(10, 0,
                future.getDayOfMonth(),
                future.getMonthValue(),
                future.getYear());
        t.setenddate(45);
        assertFalse(t.isend());
    }

    // ✅ الكود الجديد — حطه مكانه
    @Test
    void testIsstartReturnsTrueForPastDate() {
        t.setdate(0, 0, 1, 1, 2000);
        assertTrue(t.isstart());
    }

    @Test
    void testIsendReturnsTrueForPastDate() {
        // الـ endDateTime افتراضياً هو now() أيضاً، فلو انتظرنا لحظة سيكون ماضياً
        // نستخدم setdate بتاريخ ماضٍ
        t.setdate(0, 0, 1, 1, 2000);
        t.setenddate(1);
        assertTrue(t.isend());
    }

    @Test
    void testGetnowReturnsNonNull() {
        assertNotNull(time.getnow());
    }

    @Test
    void testToStringNotEmpty() {
        assertFalse(t.toString().isEmpty());
    }

    @Test
    void testToStringendtimeNotEmpty() {
        assertFalse(t.toStringendtime().isEmpty());
    }

    @Test
    void testEqualSameTime() {
        time t2 = new time();
        // كلاهما بنفس الـ datetime الافتراضي (now عند الإنشاء)
        // قد يختلفان بالثانية، لذا نستخدم setdate لنفس القيم
        LocalDateTime future = LocalDateTime.now().plusDays(3);
        t.setdate(9, 0, future.getDayOfMonth(), future.getMonthValue(), future.getYear());
        t2.setdate(9, 0, future.getDayOfMonth(), future.getMonthValue(), future.getYear());
        assertTrue(t.isSameTime(t2));
    }

    @Test
    void testEqualDifferentTime() {
        time t2 = new time();
        LocalDateTime future = LocalDateTime.now().plusDays(3);
        t.setdate(9, 0, future.getDayOfMonth(), future.getMonthValue(), future.getYear());
        t2.setdate(10, 0, future.getDayOfMonth(), future.getMonthValue(), future.getYear());
        assertFalse(t.isSameTime(t2));
    }
}