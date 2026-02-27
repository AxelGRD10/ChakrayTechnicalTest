package model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class AddressTest {
    @Test
    void testNoArgsConstructor() {
        Address address = new Address();

        assertNull(address.getIdUser());
        assertNull(address.getName());
        assertNull(address.getStreet());
        assertNull(address.getCountryCode());
    }

    @Test
    void testAllArgsConstructor() {
        Address address = new Address(
                1,
                "Axel",
                "Av. Siempre Viva 742",
                "MX"
        );

        assertEquals(1, address.getIdUser());
        assertEquals("Axel", address.getName());
        assertEquals("Av. Siempre Viva 742", address.getStreet());
        assertEquals("MX", address.getCountryCode());
    }

    @Test
    void testSettersAndGetters() {
        Address address = new Address();

        address.setIdUser(10);
        address.setName("Gabriel");
        address.setStreet("Calle Falsa 123");
        address.setCountryCode("US");

        assertEquals(10, address.getIdUser());
        assertEquals("Gabriel", address.getName());
        assertEquals("Calle Falsa 123", address.getStreet());
        assertEquals("US", address.getCountryCode());
    }
}
