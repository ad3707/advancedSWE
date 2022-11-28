package com.example.sweProject;

import com.example.sweProject.entities.User;
import com.example.sweProject.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONException;
import org.json.JSONObject;
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

    public static String getBearerToken() {
        HttpResponse<String> response = Unirest.post(
                        "https://dev-lb0aibabfhuc6e6j.us.auth0.com/oauth/token")
                .header("content-type", "application/json")
                .body("{\"client_id\":\"LsVAxRmvrm8yxktqXOzdDWWn6mlAxd6P\",\"client_secret\":\"DJeBImCv2Mi6Qbe3_m2mYPwAHSkuJO_YoXm_XlnWRg1B0myVdS4BPhO1BeaeCa3I\",\"audience\":\"localhost:8080\",\"grant_type\":\"client_credentials\"}")
                .asString();

        String responseBody = response.getBody();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            String accessToken = jsonResponse.getString("access_token");
            return "Bearer " + accessToken;
        } catch (JSONException err) {
            return null;
        }
    }

    // Test to see that a client receives all the users
    @Test
    void getUsers() throws Exception {
        User u = new User(3, "User1", 5, 2);

        List<User> allUsers = new ArrayList<>();

        allUsers.add(u);

        when(userRepo.findByClientId(any())).thenReturn(allUsers);

        mvc.perform(get("/users")
                        .header("authorization",
                                getBearerToken())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User1"))
                .andExpect(jsonPath("$[0].attempted", is(5)))
                .andExpect(jsonPath("$[0].correct", is(2)));
    }

    // Tests if client can post a user
    @Test
    @Transactional
    void testPostUser() throws Exception {
        User user = new User(2, "User2", 6, 2);
        when(userRepo.save(any())).thenReturn(user);
        mvc.perform(post("/users")
                        .header("authorization",
                                getBearerToken())
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
//    @Test
//    @Transactional
//    void testPostIncompleteUser() throws Exception {
//        User user = new User(2, "User2");
//        mvc.perform(post("/users")
//                        .header("authorization",
//                                getBearerToken())
//                        .content(asJsonString(user))
//                        .contentType("application/json")
//                        .accept("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.name").value("User2"))
//                .andExpect(jsonPath("$.attempted").value(0))
//                .andExpect(jsonPath("$.correct").value(0));
//    }

    @Test
    @Transactional
    void testGetSpecificUser() throws Exception {
        User u = new User(2, "User2");

        when(userRepo.findBySpecificUser(any(), any())).thenReturn(
                Optional.of(u));

        mvc.perform(get("/users/{id}", 2)
                        .header("authorization",
                                getBearerToken())
                        .content(asJsonString(u))
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
    void testUpdateUsers() throws Exception {
        User originalUser = new User(31, "User31", 5, 3);
        User newUser = new User(31, null, 6, 4);
        when(userRepo.findBySpecificUser(any(), any())).thenReturn(
                Optional.of(originalUser));

        mvc.perform(put("/users/{id}", 31)
                        .header("authorization",
                                getBearerToken())
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
    void testUpdateNonexistentUsers() throws Exception {
        User newUser = new User(32, null, 6, 4);

        mvc.perform(put("/users/{id}", 32)
                        .header("authorization",
                                getBearerToken())
                        .content(asJsonString(newUser))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    // Tests if a client can delete a user
    @Test
    @Transactional
    void testDeleteUser() throws Exception {
        User userToDelete = new User(31, "User2", 5, 3);
        when(userRepo.findBySpecificUser(any(), any())).thenReturn(
                Optional.of(userToDelete));

        mvc.perform(delete("/users/{id}", 31)
                        .header("authorization",
                                getBearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("User2"))
                .andExpect(jsonPath("$.attempted").value(5))
                .andExpect(jsonPath("$.correct").value(3));
    }

    // Tests if a client can delete a nonexistent user
    @Test
    @Transactional
    void testDeleteNonexistentUser() throws Exception {
        mvc.perform(delete("/users/{id}", 31)
                        .header("authorization",
                                getBearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }
}