package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testRecordCreation() {
        LoginRequest request = new LoginRequest("ABCD123456EF1", "myPassword");

        assertEquals("ABCD123456EF1", request.taxId());
        assertEquals("myPassword", request.password());
    }

    @Test
    void testEqualsAndHashCode() {
        LoginRequest req1 = new LoginRequest("ABCD123456EF1", "myPassword");
        LoginRequest req2 = new LoginRequest("ABCD123456EF1", "myPassword");

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        LoginRequest request = new LoginRequest("ABCD123456EF1", "myPassword");

        String result = request.toString();

        assertTrue(result.contains("ABCD123456EF1"));
        assertTrue(result.contains("myPassword"));
    }

    @Test
    void testInequality() {
        LoginRequest req1 = new LoginRequest("ABCD123456EF1", "myPassword");
        LoginRequest req2 = new LoginRequest("WXYZ987654GH2", "otherPassword");

        assertNotEquals(req1, req2);
    }
}
