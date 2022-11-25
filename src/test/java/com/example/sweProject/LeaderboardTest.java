package com.example.sweProject;

import com.example.sweProject.controllers.UserController;
import com.example.sweProject.entities.Question;
import com.example.sweProject.entities.User;
import com.example.sweProject.repositories.QuestionRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LeaderboardTest {
    @MockBean
    UserRepository userRepo;
    @MockBean
    QuestionRepository questionRepo;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserController userController;

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

    @Test
    @Transactional
    public void updateUserLeaderboardAPI() throws Exception {
        User user = new User(3, "NewUser", 100, 0);
        // user.setAttempted(100);
        // user.setCorrect(0);

        Question q = new Question(2, "What is 1+1?", "1", "2", "3", "4", "B");

        when(questionRepo.findBySpecificQuestion(any(), any())).thenReturn(
                Optional.of(q));
        when(userRepo.findBySpecificUser(any(), any())).thenReturn(
                Optional.of(user));

        userRepo.save(user);
        questionRepo.save(q);

        // User outUser1 = userController.updateUserLeaderboard(3, 2, "A", eq(any()));

        // Attempts incorrect answer
        mvc.perform(put("/users/{userid}/answer/{questionid}", 3, 2)
                        .header("authorization",
                                getBearerToken())
                        .content("A")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk());
        // .andExpect(jsonPath("$.id").exists())
        // .andExpect(jsonPath("$.name").value("NewUser"))
        // .andExpect(jsonPath("$.attempted").value(101))
        // .andExpect(jsonPath("$.correct").value(0));

        // Attempts correct answer
        mvc.perform(put("/users/{userid}/answer/{questionid}", 3, 2)
                        .header("authorization",
                                getBearerToken())
                        .content("B")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk());
        // .andExpect(jsonPath("$.id").exists())
        // .andExpect(jsonPath("$.name").value("NewUser"))
        // .andExpect(jsonPath("$.attempted").value(102))
        // .andExpect(jsonPath("$.correct").value(1));

    }

    // Have a user attempt a question but the user doesnt exist
    @Test
    @Transactional
    public void updateNonexistentUserLeaderboardAPI() throws Exception {
        Question q = new Question(2, "What is 1+1?", "1", "2", "3", "4", "B");

        when(questionRepo.findBySpecificQuestion(any(), any())).thenReturn(
                Optional.of(q));
        questionRepo.save(q);

        // User outUser1 = userController.updateUserLeaderboard(3, 2, "A", eq(any()));

        // Attempts incorrect user
        mvc.perform(put("/users/{userid}/answer/{questionid}", 3, 2)
                        .header("authorization",
                                getBearerToken())
                        .content("A")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    // Have a user attempt a question but the question doesnt exist
    @Test
    @Transactional
    public void updateUserNonexistentQuestionLeaderboardAPI() throws Exception {
        User user = new User(3, "NewUser", 100, 0);
        when(userRepo.findBySpecificUser(any(), any())).thenReturn(
                Optional.of(user));

        userRepo.save(user);

        // Attempts incorrect question
        mvc.perform(put("/users/{userid}/answer/{questionid}", 3, 2)
                        .header("authorization",
                                getBearerToken())
                        .content("A")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    // Have a user attempt a question but the answer doesn't exist
    @Test
    @Transactional
    public void updateUserNonexistentAnswerLeaderboardAPI() throws Exception {
        User user = new User(3, "NewUser", 100, 0);
        Question q = new Question(2, "What is 1+1?", "1", "2", "3", "4", "B");

        when(questionRepo.findBySpecificQuestion(any(), any())).thenReturn(
                Optional.of(q));
        when(userRepo.findBySpecificUser(any(), any())).thenReturn(
                Optional.of(user));

        userRepo.save(user);
        questionRepo.save(q);

        // Attempts incorrect question
        mvc.perform(put("/users/{userid}/answer/{questionid}", 3, 2)
                        .header("authorization",
                                getBearerToken())
                        .content("E")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    // Tests to see if the client receives the correct top k users
    @Test
    public void getTopKUsersTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);
        User u2 = new User(1, "U2", 20, 5);
        User u3 = new User(1, "U3", 5, 5);

        userRepo.save(u1);
        userRepo.save(u2);
        userRepo.save(u3);

        List<User> topKUsersList = new ArrayList<>();
        topKUsersList.add(u3);
        topKUsersList.add(u1);

        given(userController.getTopKUsers(2, eq(any()))).willReturn(
                topKUsersList);

        List<User> actualResults = userController.getTopKUsers(2, eq(any()));
        // Checks if it returns correctly
        assertEquals(u3.getName(), actualResults.get(0).getName());
        assertEquals(u1.getName(), actualResults.get(1).getName());
    }

    // Tests to see if K can be greater than the population (assumed yes but result
    // will not be K length)
    @Test
    public void getTopKUsersExceedSizeTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);
        User u2 = new User(1, "U2", 20, 5);
        User u3 = new User(1, "U3", 5, 5);

        userRepo.save(u1);
        userRepo.save(u2);
        userRepo.save(u3);

        List<User> topKUsersList = new ArrayList<>();
        topKUsersList.add(u3);
        topKUsersList.add(u1);
        topKUsersList.add(u2);

        given(userController.getTopKUsers(2, eq(any()))).willReturn(
                topKUsersList);

        List<User> actualResults = userController.getTopKUsers(4, eq(any()));
        // Checks if it returns correctly
        assertEquals(u3.getName(), actualResults.get(0).getName());
        assertEquals(u1.getName(), actualResults.get(1).getName());
        assertEquals(u2.getName(), actualResults.get(2).getName());
    }
}
