package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {

    @Test
    void testRecordCreation() {
        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(new AddressDTO(1, "Home", "Street 1", "MX"));

        UUID id = UUID.randomUUID();
        UserResponseDTO dto = new UserResponseDTO(
                id,
                "axel@example.com",
                "Axel",
                "+521234567890",
                "ABCD123456EF1",
                "2026-02-27T10:00:00",
                addresses
        );

        assertEquals(id, dto.id());
        assertEquals("axel@example.com", dto.email());
        assertEquals("Axel", dto.name());
        assertEquals("+521234567890", dto.phone());
        assertEquals("ABCD123456EF1", dto.taxId());
        assertEquals("2026-02-27T10:00:00", dto.createdAt());
        assertEquals(addresses, dto.addresses());
    }

    @Test
    void testEqualsAndHashCode() {
        List<AddressDTO> addresses1 = new ArrayList<>();
        addresses1.add(new AddressDTO(1, "Home", "Street 1", "MX"));

        List<AddressDTO> addresses2 = new ArrayList<>();
        addresses2.add(new AddressDTO(1, "Home", "Street 1", "MX"));

        UUID id = UUID.randomUUID();

        UserResponseDTO dto1 = new UserResponseDTO(id, "axel@example.com", "Axel", "+521234567890",
                "ABCD123456EF1", "2026-02-27T10:00:00", addresses1);

        UserResponseDTO dto2 = new UserResponseDTO(id, "axel@example.com", "Axel", "+521234567890",
                "ABCD123456EF1", "2026-02-27T10:00:00", addresses2);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(new AddressDTO(1, "Home", "Street 1", "MX"));

        UUID id = UUID.randomUUID();
        UserResponseDTO dto = new UserResponseDTO(
                id,
                "axel@example.com",
                "Axel",
                "+521234567890",
                "ABCD123456EF1",
                "2026-02-27T10:00:00",
                addresses
        );

        String result = dto.toString();

        assertTrue(result.contains("axel@example.com"));
        assertTrue(result.contains("Axel"));
        assertTrue(result.contains("+521234567890"));
        assertTrue(result.contains("ABCD123456EF1"));
        assertTrue(result.contains("2026-02-27T10:00:00"));
    }

    @Test
    void testInequality() {
        List<AddressDTO> addresses1 = new ArrayList<>();
        addresses1.add(new AddressDTO(1, "Home", "Street 1", "MX"));

        List<AddressDTO> addresses2 = new ArrayList<>();
        addresses2.add(new AddressDTO(2, "Office", "Street 2", "US"));

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        UserResponseDTO dto1 = new UserResponseDTO(id1, "axel@example.com", "Axel", "+521234567890",
                "ABCD123456EF1", "2026-02-27T10:00:00", addresses1);

        UserResponseDTO dto2 = new UserResponseDTO(id2, "gabriel@example.com", "Gabriel", "+521098765432",
                "WXYZ987654GH2", "2026-02-27T11:00:00", addresses2);

        assertNotEquals(dto1, dto2);
    }
}
