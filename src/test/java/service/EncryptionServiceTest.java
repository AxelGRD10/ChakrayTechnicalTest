package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
    }

    @Test
    void testEncryptNotNull() {
        String password = "myPassword123";
        String encrypted = encryptionService.encrypt(password);

        assertNotNull(encrypted);
        assertNotEquals(password, encrypted, "Encrypted password should not be equal to original");
    }

    @Test
    void testEncryptDifferentPasswordsProduceDifferentResults() {
        String password1 = "password1";
        String password2 = "password2";

        String encrypted1 = encryptionService.encrypt(password1);
        String encrypted2 = encryptionService.encrypt(password2);

        assertNotEquals(encrypted1, encrypted2, "Different passwords should produce different encrypted values");
    }

    @Test
    void testEncryptSamePasswordProducesSameResult() {
        String password = "consistentPassword";

        String encrypted1 = encryptionService.encrypt(password);
        String encrypted2 = encryptionService.encrypt(password);

        assertEquals(encrypted1, encrypted2, "Same password should produce the same encrypted value");
    }

    @Test
    void testEncryptEmptyPassword() {
        String password = "";
        String encrypted = encryptionService.encrypt(password);

        assertNotNull(encrypted);
        assertNotEquals(password, encrypted);
    }

    @Test
    void testEncryptNullPasswordThrowsException() {
        assertThrows(RuntimeException.class, () -> encryptionService.encrypt(null));
    }
}
