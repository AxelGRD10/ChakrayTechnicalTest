package mapper;

import model.Address;
import model.AddressDTO;
import model.User;
import model.UserResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void testToDTO() {
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(1, "Home", "Street 1", "MX"));
        addresses.add(new Address(2, "Office", "Street 2", "US"));

        UUID userId = UUID.randomUUID();
        User user = new User(
                userId,
                "axel@example.com",
                "Axel",
                "+521234567890",
                "password123",
                "ABCD123456EF1",
                "27-02-2026 10:00",
                addresses
        );

        UserResponseDTO dto = UserMapper.toDTO(user);

        assertEquals(user.getId(), dto.id());
        assertEquals(user.getEmail(), dto.email());
        assertEquals(user.getName(), dto.name());
        assertEquals(user.getPhone(), dto.phone());
        assertEquals(user.getTaxId(), dto.taxId());
        assertEquals(user.getCreatedAt(), dto.createdAt());

        assertEquals(user.getAddresses().size(), dto.addresses().size());

        for (int i = 0; i < addresses.size(); i++) {
            Address original = addresses.get(i);
            AddressDTO mapped = dto.addresses().get(i);

            assertEquals(original.getIdUser(), mapped.id());
            assertEquals(original.getName(), mapped.name());
            assertEquals(original.getStreet(), mapped.street());
            assertEquals(original.getCountryCode(), mapped.countryCode());
        }
    }

    @Test
    void testToDTOWithEmptyAddresses() {
        User user = new User(
                UUID.randomUUID(),
                "axel@example.com",
                "Axel",
                "+521234567890",
                "password123",
                "ABCD123456EF1",
                "27-02-2026 10:00",
                new ArrayList<>()
        );

        UserResponseDTO dto = UserMapper.toDTO(user);

        assertNotNull(dto.addresses());
        assertTrue(dto.addresses().isEmpty());
    }
}
