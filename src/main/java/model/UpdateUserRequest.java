package model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdateUserRequest(@Email(message = "Invalid email format")
                                String email,

                                String name,

                                @Pattern(
                                        regexp = "^\\+?[0-9]{10,15}$",
                                        message = "Phone must be 10-15 digits and may include country code"
                                )
                                String phone,

                                String password,

                                @Pattern(
                                        regexp = "^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$",
                                        message = "tax_id must have RFC format"
                                )
                                String taxId) {
}
