package model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class AddressDTOTest {
    @Test
    void testRecordCreation() {
        AddressDTO dto = new AddressDTO(
                1,
                "Axel",
                "Av. Siempre Viva 742",
                "MX"
        );

        assertEquals(1, dto.id());
        assertEquals("Axel", dto.name());
        assertEquals("Av. Siempre Viva 742", dto.street());
        assertEquals("MX", dto.countryCode());
    }

    @Test
    void testEqualsAndHashCode() {
        AddressDTO dto1 = new AddressDTO(1, "Axel", "Street 1", "MX");
        AddressDTO dto2 = new AddressDTO(1, "Axel", "Street 1", "MX");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AddressDTO dto = new AddressDTO(1, "Axel", "Street 1", "MX");

        String result = dto.toString();

        assertTrue(result.contains("1"));
        assertTrue(result.contains("Axel"));
        assertTrue(result.contains("Street 1"));
        assertTrue(result.contains("MX"));
    }

    @Test
    void testInequality() {
        AddressDTO dto1 = new AddressDTO(1, "Axel", "Street 1", "MX");
        AddressDTO dto2 = new AddressDTO(2, "Gabriel", "Street 2", "US");

        assertNotEquals(dto1, dto2);
    }
}
