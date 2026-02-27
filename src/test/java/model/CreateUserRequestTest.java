package model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWhenDataIsValid() {
        CreateUserRequest request = new CreateUserRequest(
                "axel@example.com",
                "Axel",
                "+521234567890",
                "password123",
                "ABCD123456EF1"
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        CreateUserRequest request = new CreateUserRequest(
                "invalid-email",
                "Axel",
                "+521234567890",
                "password123",
                "ABCD123456EF1"
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        CreateUserRequest request = new CreateUserRequest(
                "axel@example.com",
                "",
                "+521234567890",
                "password123",
                "ABCD123456EF1"
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("name"))
        );
    }

    @Test
    void shouldFailWhenPhoneIsInvalid() {
        CreateUserRequest request = new CreateUserRequest(
                "axel@example.com",
                "Axel",
                "1234",
                "password123",
                "ABCD123456EF1"
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("phone"))
        );
    }

    @Test
    void shouldFailWhenTaxIdIsInvalid() {
        CreateUserRequest request = new CreateUserRequest(
                "axel@example.com",
                "Axel",
                "+521234567890",
                "password123",
                "INVALIDRFC"
        );

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("taxId"))
        );
    }
}