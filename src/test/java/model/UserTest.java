package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testNoArgsConstructor() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getName());
        assertNull(user.getPhone());
        assertNull(user.getPassword());
        assertNull(user.getTaxId());
        assertNull(user.getCreatedAt());
        assertNull(user.getAddresses());
    }

    @Test
    void testAllArgsConstructor() {
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(1, "Home", "Street 1", "MX"));

        UUID id = UUID.randomUUID();
        User user = new User(
                id,
                "axel@example.com",
                "Axel",
                "+521234567890",
                "password123",
                "ABCD123456EF1",
                "2026-02-27T10:00:00",
                addresses
        );

        assertEquals(id, user.getId());
        assertEquals("axel@example.com", user.getEmail());
        assertEquals("Axel", user.getName());
        assertEquals("+521234567890", user.getPhone());
        assertEquals("password123", user.getPassword());
        assertEquals("ABCD123456EF1", user.getTaxId());
        assertEquals("2026-02-27T10:00:00", user.getCreatedAt());
        assertEquals(addresses, user.getAddresses());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setEmail("gabriel@example.com");
        user.setName("Gabriel");
        user.setPhone("+521098765432");
        user.setPassword("pass456");
        user.setTaxId("WXYZ987654GH2");
        user.setCreatedAt("2026-02-27T11:00:00");

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(2, "Office", "Street 2", "US"));
        user.setAddresses(addresses);

        assertEquals(id, user.getId());
        assertEquals("gabriel@example.com", user.getEmail());
        assertEquals("Gabriel", user.getName());
        assertEquals("+521098765432", user.getPhone());
        assertEquals("pass456", user.getPassword());
        assertEquals("WXYZ987654GH2", user.getTaxId());
        assertEquals("2026-02-27T11:00:00", user.getCreatedAt());
        assertEquals(addresses, user.getAddresses());
    }
}