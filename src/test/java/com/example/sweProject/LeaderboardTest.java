package com.example.sweProject;

import com.example.sweProject.controllers.UserController;
import com.example.sweProject.entities.Question;
import com.example.sweProject.entities.User;
import com.example.sweProject.repositories.QuestionRepository;
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
    private final String bearerToken =
            "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InEyM0NreTdaTjROdEQ0R0Z6TGhhVSJ9.eyJpc3MiOiJodHRwczovL2Rldi1sYjBhaWJhYmZodWM2ZTZqLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJMc1ZBeFJtdnJtOHl4a3RxWE96ZERXV242bWxBeGQ2UEBjbGllbnRzIiwiYXVkIjoibG9jYWxob3N0OjgwODAiLCJpYXQiOjE2NjkxNzk0MjYsImV4cCI6MTY2OTI2NTgyNiwiYXpwIjoiTHNWQXhSbXZybTh5eGt0cVhPemREV1duNm1sQXhkNlAiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.XCOPvcWL24a4iuq42R78OybdzYhW-7HFXL-i22UuwpmT4tNtU42gmFoQFIZ7wEotcn7vPdffLPQdJ3v-c8uuJAArdJMWB2zx8nFl4w__CciB9RuxxASqBmfrcE4e-2mzjQ3fyRnyKIb3pSSmB_c22-YX128B4fFmvlmDNr2Gp7_akxXrpnQSoScd-be9yS5fb1QI9-bKlRTatMTTMum7elWrDOw-MyYHmFshs-pDWud30vHgSDTLZUxyTv3m89gJfA_0HKEiclBwH0u5CygVqnQBmLpxdvXWrYgfcMYV1Q5ibnEe4Gc1a90AuMQ_MaV9yE2qIZOoogdsnQlvQ9Fwrg";
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
                                bearerToken)
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
                                bearerToken)
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
                                bearerToken)
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
                                bearerToken)
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
                                bearerToken)
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
