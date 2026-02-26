package service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {
    private static final String SECRET_KEY =
            "12345678901234567890123456789012";

    public String encrypt(String password) {
        try {
            SecretKeySpec key =
                    new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encrypted =
                    cipher.doFinal(password.getBytes());

            return Base64.getEncoder()
                    .encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password");
        }
    }
}
