package model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWhenDataIsValid() {
        UpdateUserRequest request = new UpdateUserRequest(
                "axel@example.com",
                "Axel",
                "+521234567890",
                "password123",
                "ABCD123456EF1"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        UpdateUserRequest request = new UpdateUserRequest(
                "invalid-email",
                "Axel",
                "+521234567890",
                "password123",
                "ABCD123456EF1"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                                && v.getMessage().equals("Invalid email format"))
        );
    }

    @Test
    void shouldFailWhenPhoneIsInvalid() {
        UpdateUserRequest request = new UpdateUserRequest(
                "axel@example.com",
                "Axel",
                "1234",
                "password123",
                "ABCD123456EF1"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("phone")
                                && v.getMessage().equals("Phone must be 10-15 digits and may include country code"))
        );
    }

    @Test
    void shouldFailWhenTaxIdIsInvalid() {
        UpdateUserRequest request = new UpdateUserRequest(
                "axel@example.com",
                "Axel",
                "+521234567890",
                "password123",
                "INVALIDRFC"
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("taxId")
                                && v.getMessage().equals("tax_id must have RFC format"))
        );
    }

    @Test
    void shouldAllowOptionalFieldsEmpty() {
        UpdateUserRequest request = new UpdateUserRequest(
                "axel@example.com",
                null,
                null,
                null,
                null
        );

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty() || violations.stream().allMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
}