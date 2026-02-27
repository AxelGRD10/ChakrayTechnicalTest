package service;

import model.UpdateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private EncryptionService encryptionService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        encryptionService = mock(EncryptionService.class);
        when(encryptionService.encrypt(anyString())).thenAnswer(inv -> "enc:" + inv.getArgument(0));
        userService = new UserService(encryptionService);
    }

    @Test
    void testCreateUserSuccess() {
        String email = "newuser@mail.com";
        String name = "New User";
        String phone = "+5215555555555";
        String password = "pass123";
        String taxId = "ZZZZ990101ZZZ";

        var user = userService.createUser(email, name, phone, password, taxId);

        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
        assertEquals(phone, user.getPhone());
        assertEquals("enc:" + password, user.getPassword());
        assertEquals(taxId, user.getTaxId());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testCreateUserDuplicateTaxIdThrowsException() {
        String existingTaxId = "AAAA990101AAA"; // from initial users
        assertThrows(IllegalArgumentException.class, () ->
                userService.createUser("test@mail.com", "Test", "+5215000000000", "pass", existingTaxId)
        );
    }

    @Test
    void testGetUsersSortedByEmail() {
        var sorted = userService.getUsers("email");
        assertEquals(3, sorted.size()); // initial users
        assertTrue(sorted.get(0).getEmail().compareTo(sorted.get(1).getEmail()) <= 0);
    }

    @Test
    void testUpdateUserSuccess() {
        var existingUser = userService.getUsers(null).get(0);
        UUID userId = existingUser.getId();

        UpdateUserRequest request = new UpdateUserRequest(
                "updated@mail.com",
                "Updated Name",
                "+5215000000000",
                "newpass",
                "UNIQ990101XXX"
        );

        when(encryptionService.encrypt("newpass")).thenReturn("enc:newpass");

        var updatedDTO = userService.updateUser(userId, request);

        assertEquals("updated@mail.com", updatedDTO.email());
        assertEquals("Updated Name", updatedDTO.name());
        assertEquals("+5215000000000", updatedDTO.phone());
        assertEquals("UNIQ990101XXX", updatedDTO.taxId());
    }

    @Test
    void testUpdateUserDuplicateTaxIdThrowsException() {
        var users = userService.getUsers(null);
        var user1 = users.get(0);
        var user2 = users.get(1);

        UpdateUserRequest request = new UpdateUserRequest(
                null,
                null,
                null,
                null,
                user2.getTaxId()
        );

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user1.getId(), request));
    }

    @Test
    void testDeleteUserSuccess() {
        var user = userService.getUsers(null).get(0);
        UUID userId = user.getId();

        userService.deleteUser(userId);

        assertFalse(userService.getUsers(null).stream()
                .anyMatch(u -> u.getId().equals(userId)));
    }

    @Test
    void testDeleteUserNotFoundThrowsException() {
        assertThrows(NoSuchElementException.class, () ->
                userService.deleteUser(UUID.randomUUID())
        );
    }

    @Test
    void testFilterUsersContains() {
        var results = userService.filterUsers("name+co+One");
        assertEquals(1, results.size());
        assertTrue(results.get(0).name().contains("One"));
    }

    @Test
    void testFilterUsersInvalidFormatThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                userService.filterUsers("invalid-filter")
        );
    }

    @Test
    void testLoginNonExistentUserThrowsException() {
        assertThrows(ResponseStatusException.class, () ->
                userService.login("NONEXISTENT", "123")
        );
    }
}
