package controller;

import jakarta.validation.Valid;
import mapper.UserMapper;
import model.CreateUserRequest;
import model.UpdateUserRequest;
import model.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class userController {

    private final UserService userService;

    public userController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/userSortedBy")
    public ResponseEntity<List<UserResponseDTO>> getUsersSortedBy(
            @RequestParam(required = false) String sortedBy) {

        var users = userService.getUsers(sortedBy)
                .stream()
                .map(UserMapper::toDTO)
                .toList();

        return ResponseEntity.ok(users);
    }
    @PostMapping("/newUser")
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        var user = userService.createUser(
                request.email(),
                request.name(),
                request.phone(),
                request.password(),
                request.taxId()
        );

        return ResponseEntity.ok(
                UserMapper.toDTO(user)
        );
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(userService.updateUser(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("userFilter")
    public ResponseEntity<List<UserResponseDTO>> getUsersByFilter(
            @RequestParam String filter) {

        return ResponseEntity.ok(userService.getUsersByFilter(filter));
    }
}
