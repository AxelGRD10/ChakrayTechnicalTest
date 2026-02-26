package model;

import java.util.List;
import java.util.UUID;

public record UserResponseDTO(UUID id,
                              String email,
                              String name,
                              String phone,
                              String taxId,
                              String createdAt,
                              List<AddressDTO> addresses) {
}
