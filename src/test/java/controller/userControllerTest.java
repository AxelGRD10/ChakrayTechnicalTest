package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import service.UserService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class userControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController controller = new userController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetUsersSortedBy() throws Exception {
        UUID id = UUID.randomUUID();
        var dto = new UserResponseDTO(id, "email@mail.com", "Name", "+5212345678", "ABCD123456EF1", "27-02-2026 10:00", List.of());
        when(userService.getUsers("email")).thenReturn(List.of(
                new User(id, "email@mail.com", "Name", "+5212345678", "pass", "ABCD123456EF1", "27-02-2026 10:00", List.of())
        ));

        mockMvc.perform(get("/api/v1/user/userSortedBy")
                        .param("sortedBy", "email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("email@mail.com"));

        verify(userService, times(1)).getUsers("email");
    }

    @Test
    void testCreateUser() throws Exception {
        UUID id = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest(
                "new@mail.com", "New User", "+5212345678", "pass123", "ZZZZ990101XXX"
        );
        var user = new User(id, request.email(), request.name(), request.phone(), request.password(), request.taxId(), "27-02-2026 10:00", List.of());
        when(userService.createUser(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/api/v1/user/newUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@mail.com"))
                .andExpect(jsonPath("$.name").value("New User"));

        verify(userService, times(1))
                .createUser(eq("new@mail.com"), eq("New User"), eq("+5212345678"), eq("pass123"), eq("ZZZZ990101XXX"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest("updated@mail.com", "Updated", "+521555555555", "newpass", "YYYY990101XXX");
        UserResponseDTO dto = new UserResponseDTO(id, request.email(), request.name(), request.phone(), request.taxId(), "27-02-2026 11:00", List.of());
        when(userService.updateUser(eq(id), any(UpdateUserRequest.class))).thenReturn(dto);

        mockMvc.perform(patch("/api/v1/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@mail.com"))
                .andExpect(jsonPath("$.name").value("Updated"));

        verify(userService, times(1)).updateUser(eq(id), any(UpdateUserRequest.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/user/{id}", id))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(id);
    }


    @Test
    void testLogin() throws Exception {
        UUID id = UUID.randomUUID();
        LoginRequest request = new LoginRequest("ABCD123456EF1", "pass123");
        var dto = new UserResponseDTO(id, "email@mail.com", "Name", "+5212345678", request.taxId(), "27-02-2026 10:00", List.of());

        when(userService.login(eq("ABCD123456EF1"), eq("pass123"))).thenReturn(
                new UserResponseDTO(id, "email@mail.com", "Name", "+5212345678", "ABCD123456EF1", "27-02-2026 10:00", List.of())
        );

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taxId").value("ABCD123456EF1"));

        verify(userService, times(1)).login(eq("ABCD123456EF1"), eq("pass123"));
    }
}
