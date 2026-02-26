package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String email;
    private String name;
    private String phone;
    private String password;
    private String taxId;
    private String createdAt;
    private List<Address> addresses;
}
