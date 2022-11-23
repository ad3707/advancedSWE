package com.example.sweProject;

import com.example.sweProject.entities.User;
import com.example.sweProject.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private final String bearerToken =
            "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InEyM0NreTdaTjROdEQ0R0Z6TGhhVSJ9.eyJpc3MiOiJodHRwczovL2Rldi1sYjBhaWJhYmZodWM2ZTZqLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJMc1ZBeFJtdnJtOHl4a3RxWE96ZERXV242bWxBeGQ2UEBjbGllbnRzIiwiYXVkIjoibG9jYWxob3N0OjgwODAiLCJpYXQiOjE2NjkxNzk0MjYsImV4cCI6MTY2OTI2NTgyNiwiYXpwIjoiTHNWQXhSbXZybTh5eGt0cVhPemREV1duNm1sQXhkNlAiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.XCOPvcWL24a4iuq42R78OybdzYhW-7HFXL-i22UuwpmT4tNtU42gmFoQFIZ7wEotcn7vPdffLPQdJ3v-c8uuJAArdJMWB2zx8nFl4w__CciB9RuxxASqBmfrcE4e-2mzjQ3fyRnyKIb3pSSmB_c22-YX128B4fFmvlmDNr2Gp7_akxXrpnQSoScd-be9yS5fb1QI9-bKlRTatMTTMum7elWrDOw-MyYHmFshs-pDWud30vHgSDTLZUxyTv3m89gJfA_0HKEiclBwH0u5CygVqnQBmLpxdvXWrYgfcMYV1Q5ibnEe4Gc1a90AuMQ_MaV9yE2qIZOoogdsnQlvQ9Fwrg";
    // Used to add mock objects
    @MockBean
    UserRepository userRepo;
    // Inject object dependency implicitly
    @Autowired
    private MockMvc mvc;

    public static String asJsonString(final Object question) {
        try {
            return new ObjectMapper().writeValueAsString(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Test to see that a client receives all the users
    @Test
    public void getUsers() throws Exception {
        User u = new User(3, "User1", 5, 2);
        List<User> allUsers = new ArrayList<>();
        allUsers.add(u);
        when(userRepo.findByClientId(any())).thenReturn(allUsers);

        mvc.perform(get("/users")
                        .header("authorization",
                                bearerToken)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User1"))
                .andExpect(jsonPath("$[0].attempted", is(5)))
                .andExpect(jsonPath("$[0].correct", is(2)));
    }

    // Tests if client can post a user
    @Test
    @Transactional
    public void testPostUser() throws Exception {
        User user = new User(2, "User2", 6, 2);
        when(userRepo.save(any())).thenReturn(user);
        mvc.perform(post("/users")
                        .header("authorization",
                                bearerToken)
                        .content(asJsonString(user))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("User2"))
                .andExpect(jsonPath("$.attempted").value(6))
                .andExpect(jsonPath("$.correct").value(2));
    }

    // Tests if client can post an incomplete user (no attempted/correct initially
    // defined)
    @Test
    @Transactional
    public void testPostIncompleteUser() throws Exception {
        User user = new User(2, "User2");
        when(userRepo.save(any())).thenReturn(user);
        mvc.perform(post("/users")
                        .header("authorization",
                                bearerToken)
                        .content(asJsonString(user))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("User2"))
                .andExpect(jsonPath("$.attempted").value(0))
                .andExpect(jsonPath("$.correct").value(0));
    }

    // Tests if client can update a user
    @Test
    @Transactional
    // ensures that the interactions you have with the database are rolled back at
    // the end of each test
    public void testUpdateUsers() throws Exception {
        User originalUser = new User(31, "User31", 5, 3);
        User newUser = new User(31, null, 6, 4);
        when(userRepo.findBySpecificUser(any(), any())).thenReturn(
                Optional.of(originalUser));

        mvc.perform(put("/users/{id}", 31)
                        .header("authorization",
                                bearerToken)
                        .content(asJsonString(newUser))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("User31"))
                .andExpect(jsonPath("$.attempted").value("6"))
                .andExpect(jsonPath("$.correct").value("4"));
    }

    // Tests if client can update a nonexistent user
    @Test
    @Transactional
    // ensures that the interactions you have with the database are rolled back at
    // the end of each test
    public void testUpdateNonexistentUsers() throws Exception {
        User newUser = new User(32, null, 6, 4);

        mvc.perform(put("/users/{id}", 32)
                        .header("authorization",
                                bearerToken)
                        .content(asJsonString(newUser))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    // Tests if a client can delete a user
    @Test
    @Transactional
    public void testDeleteUser() throws Exception {
        User userToDelete = new User(31, "User2", 5, 3);
        when(userRepo.findBySpecificUser(any(), any())).thenReturn(
                Optional.of(userToDelete));

        mvc.perform(delete("/users/{id}", 31)
                        .header("authorization",
                                bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("User2"))
                .andExpect(jsonPath("$.attempted").value(5))
                .andExpect(jsonPath("$.correct").value(3));
    }

    // Tests if a client can delete a nonexistent user
    @Test
    @Transactional
    public void testDeleteNonexistentUser() throws Exception {

        mvc.perform(delete("/users/{id}", 31)
                        .header("authorization",
                                bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }
}