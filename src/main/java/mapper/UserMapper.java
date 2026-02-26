package mapper;

import model.AddressDTO;
import model.User;
import model.UserResponseDTO;

import java.util.List;

public class UserMapper {
    public static UserResponseDTO toDTO(User user) {

        List<AddressDTO> addresses = user.getAddresses()
                .stream()
                .map(a -> new AddressDTO(
                        a.getIdUser(),
                        a.getName(),
                        a.getStreet(),
                        a.getCountryCode()))
                .toList();

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getTaxId(),
                user.getCreatedAt(),
                addresses
        );
    }
}
