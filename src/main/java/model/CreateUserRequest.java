package model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(@Email
                                @NotBlank
                                String email,

                                @NotBlank
                                String name,

                                @Pattern(regexp = "^\\+?\\d{10,15}$",
                                        message = "Invalid phone format")
                                String phone,

                                @NotBlank
                                String password,

                                @Pattern(regexp = "^[A-Z]{4}\\d{6}[A-Z0-9]{3}$",
                                        message = "Invalid RFC format")
                                String taxId) {
}
