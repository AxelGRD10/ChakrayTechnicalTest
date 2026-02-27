package service;

import mapper.UserMapper;
import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();
    private final EncryptionService encryptionService;
    private static final String SECRET_KEY = "12345678901234567890123456789012";

    public UserService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
        loadInitialUsers();
    }

    private void loadInitialUsers() {

        users.add(buildInitialUser(
                "user1@mail.com",
                "User One",
                "+5215511111111",
                "AAAA990101AAA"
        ));

        users.add(buildInitialUser(
                "user2@mail.com",
                "User Two",
                "+5215522222222",
                "BBBB990101BBB"
        ));

        users.add(buildInitialUser(
                "user3@mail.com",
                "User Three",
                "+5215533333333",
                "CCCC990101CCC"
        ));
    }
    private User buildInitialUser(String email,
                                  String name,
                                  String phone,
                                  String taxId) {

        List<Address> addresses = List.of(
                new Address(1, "workaddress", "street No. 1", "UK"),
                new Address(2, "homeaddress", "street No. 2", "AU")
        );

        return new User(
                UUID.randomUUID(),
                email,
                name,
                phone,
                encryptionService.encrypt("123456"),
                taxId,
                getMadagascarTime(),
                new ArrayList<>(addresses)
        );
    }
    public User createUser(String email, String name,
                           String phone, String password,
                           String taxId) {

        validateUniqueTaxId(taxId);

        String encryptedPassword =
                encryptionService.encrypt(password);

        String createdAt = LocalDateTime.now(
                        ZoneId.of("Indian/Antananarivo"))
                .format(DateTimeFormatter
                        .ofPattern("dd-MM-yyyy HH:mm"));

        User user = new User(
                UUID.randomUUID(),
                email,
                name,
                phone,
                encryptedPassword,
                taxId,
                createdAt,
                new ArrayList<>()
        );
        users.add(user);

        return user;
    }

    public List<User> getUsers(String sortedBy) {

        if (sortedBy == null || sortedBy.isBlank()) {
            return new ArrayList<>(users);
        }

        UserSortField field =
                UserSortField.valueOf(sortedBy.toLowerCase());

        Comparator<User> comparator = switch (field) {
            case email -> Comparator.comparing(User::getEmail);
            case id -> Comparator.comparing(User::getId);
            case name -> Comparator.comparing(User::getName);
            case phone -> Comparator.comparing(User::getPhone);
            case tax_id -> Comparator.comparing(User::getTaxId);
            case created_at -> Comparator.comparing(User::getCreatedAt);
        };

        return users.stream()
                .sorted(comparator)
                .toList();
    }

    private void validateUniqueTaxId(String taxId) {
        boolean exists = users.stream()
                .anyMatch(u -> u.getTaxId().equals(taxId));

        if (exists) {
            throw new IllegalArgumentException("tax_id must be unique");
        }
    }
    private String getMadagascarTime() {
        ZoneId madagascarZone = ZoneId.of("Indian/Antananarivo");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.now(madagascarZone).format(formatter);
    }
    public UserResponseDTO updateUser(UUID id, UpdateUserRequest request) {

        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.phone() != null) {
            user.setPhone(request.phone());
        }

        if (request.password() != null) {
            user.setPassword(encryptionService.encrypt(request.password()));
        }

        if (request.taxId() != null) {

            boolean exists = users.stream()
                    .anyMatch(u -> !u.getId().equals(id)
                            && u.getTaxId().equalsIgnoreCase(request.taxId()));

            if (exists) {
                throw new IllegalArgumentException("tax_id must be unique");
            }

            user.setTaxId(request.taxId());
        }

        return mapToDTO(user);
    }
    private UserResponseDTO mapToDTO(User user) {

        List<AddressDTO> addressDTOs = user.getAddresses()
                .stream()
                .map(address -> new AddressDTO(
                        address.getIdUser(),
                        address.getName(),
                        address.getStreet(),
                        address.getCountryCode()
                ))
                .toList();

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getTaxId(),
                user.getCreatedAt(),
                addressDTOs
        );
    }
    public void deleteUser(UUID idUser){
        boolean removed = users.removeIf(user -> user.getId().equals(idUser));

        if (!removed) {
            throw new NoSuchElementException("User not found");
        }
    }

    public List<UserResponseDTO> filterUsers(String filter) {

        if (filter == null || filter.isBlank()) {
            throw new IllegalArgumentException("Filter must not be null or empty");
        }

        String[] parts = filter.split("\\+");

        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Invalid filter format. Expected: field+operator+value");
        }

        String field = parts[0].toLowerCase();
        String operator = parts[1].toLowerCase();
        String value = parts[2].toLowerCase();

        validateField(field);
        validateOperator(operator);

        return users.stream()
                .filter(user -> matches(user, field, operator, value))
                .map(this::mapToDTO)
                .toList();
    }

    private boolean matches(User user, String field, String operator, String value) {

        String fieldValue = getFieldValue(user, field);

        if (fieldValue == null) return false;

        fieldValue = fieldValue.toLowerCase();

        return switch (operator) {
            case "co" -> fieldValue.contains(value);
            case "eq" -> fieldValue.equals(value);
            case "sw" -> fieldValue.startsWith(value);
            case "ew" -> fieldValue.endsWith(value);
            default -> false;
        };
    }

    private String getFieldValue(User user, String field) {

        return switch (field) {
            case "id" -> user.getId().toString();
            case "email" -> user.getEmail();
            case "name" -> user.getName();
            case "phone" -> user.getPhone();
            case "tax_id" -> user.getTaxId();
            case "created_at" -> user.getCreatedAt().toString();
            default -> null;
        };
    }

    private void validateField(String field) {
        List<String> allowedFields = List.of(
                "id", "email", "name", "phone", "tax_id", "created_at");

        if (!allowedFields.contains(field)) {
            throw new IllegalArgumentException("Invalid field: " + field);
        }
    }

    private void validateOperator(String operator) {
        List<String> allowedOperators = List.of("co", "eq", "sw", "ew");

        if (!allowedOperators.contains(operator)) {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
    public UserResponseDTO login(String taxId, String password) {

        var user = users.stream()
                .filter(u -> u.getTaxId().equalsIgnoreCase(taxId))
                .findFirst()
                .orElse(null);

        System.out.println("Attempting login for taxId: " + taxId);

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }

        String decryptedPassword = aesDecrypt(user.getPassword());

        if (!decryptedPassword.equals(password)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }

        return UserMapper.toDTO(user);
    }

    private String aesDecrypt(String encryptedPassword) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            SecretKeySpec keySpec = new SecretKeySpec(
                    SECRET_KEY.getBytes(StandardCharsets.UTF_8),
                    "AES"
            );

            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);

            byte[] decrypted = cipher.doFinal(decodedBytes);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password");
        }
    }
}
