package com.example.hng_task2.auth;

import com.example.hng_task2.dto.LoginRequest;
import com.example.hng_task2.dto.RegisterRequest;
import com.example.hng_task2.entity.User;
import com.example.hng_task2.repository.OrganisationRepository;
import com.example.hng_task2.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class E2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        clearDatabase();
    }

    @AfterEach
    void tearDown() {
        clearDatabase();
    }

    private void clearDatabase() {
        organisationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "password123",
                "08123456789"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.data.user.firstName").value("John"))
                .andExpect(jsonPath("$.data.user.lastName").value("Doe"))
                .andExpect(jsonPath("$.data.user.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.data.user.phone").value("08123456789"));
    }

    @Test
    void shouldLoginUserSuccessfully() throws Exception {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password(passwordEncoder.encode("password123"))
                .phone("08123456789")
                .build();
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.user.firstName").value("John"))
                .andExpect(jsonPath("$.data.user.lastName").value("Doe"))
                .andExpect(jsonPath("$.data.user.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.data.user.phone").value("08123456789"));
    }

    @Test
    void shouldFailIfRequiredFieldsAreMissingDuringRegistration() throws Exception {
        // Act: missing firstName field
        RegisterRequest registerRequest = new RegisterRequest(
                "", // missing firstName
                "Doe",
                "john.doe1@example.com",
                "password123",
                "08123456789"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isUnprocessableEntity());
//                .andExpect(jsonPath("$.status").value("fail"))
//                .andExpect(jsonPath("$.errors.firstName").value("First name is required"));

        // Act: missing lastName field
        registerRequest = new RegisterRequest(
                "John",
                "", // missing lastName
                "john.doe@example.com",
                "password123",
                "08123456789"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isUnprocessableEntity());

        // Act: missing email field
        registerRequest = new RegisterRequest(
                "John",
                "Doe",
                "", // missing email
                "password123",
                "08123456789"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isUnprocessableEntity());

        // Act: missing password field
        registerRequest = new RegisterRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "" ,// missing password,
                "08123456789"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    void shouldFailIfRequiredFieldsAreMissingDuringLogin() throws Exception {
        // Act: missing email field
        LoginRequest loginRequest = new LoginRequest(
                "",
                "password123"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnprocessableEntity());

        // Act: missing password field
        loginRequest = new LoginRequest(
                "john.doe@example.com",
                ""
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnprocessableEntity());


    }

    @Test
    void shouldFailIfDuplicateEmail() throws Exception {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password(passwordEncoder.encode("password123"))
                .build();
        userRepository.save(user);

        // An attempt to register with the same email should fail
        RegisterRequest registerRequest = new RegisterRequest(
                "Jane",
                "Doe",
                "john.doe@example.com", // duplicate email
                "password123",
                "08123456789"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isUnprocessableEntity());
    }
}
