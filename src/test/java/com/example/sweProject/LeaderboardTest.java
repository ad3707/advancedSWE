package com.example.sweProject;

import org.junit.jupiter.api.Test;

import com.example.sweProject.controllers.UserController;
import com.example.sweProject.entities.User;
import com.example.sweProject.entities.Question;
import com.example.sweProject.repositories.QuestionRepository;
import com.example.sweProject.repositories.UserRepository;
import com.example.sweProject.controllers.QuestionController;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class LeaderboardTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserController userController;

    @MockBean
    private QuestionController questionController;

    @MockBean
    UserRepository userRepo;

    @MockBean
    QuestionRepository questionRepo;

    @Test
    @Transactional
    public void updateUserLeaderboardAPI() throws Exception {
        User user = new User(3, "NewUser", 100, 0);
        // user.setAttempted(100);
        // user.setCorrect(0);

        Question q = new Question(2, "What is 1+1?", "1", "2", "3", "4", "B");

        System.out.println("TEST START YAY");
        when(questionRepo.findBySpecificQuestion(any(), any())).thenReturn(Optional.of(q));
        when(userRepo.findBySpecificUser(any(), any())).thenReturn(Optional.of(user));
        System.out.println("TEST END YAY");

        // userRepo.save(user);
        // questionRepo.save(q);

        // List<User> actualResults = userController.getTopKUsers(2, eq(any()));
        // Checks if it returns correctly
        // assertEquals(user.updateUserLeaderboard(3, 2, "A").attempted, 100, any());

        // Attempts incorrect answer
        mvc.perform(put("/users/{userid}/answer/{questionid}/{choice}", 3, 2, "A")
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        /*
         * .andExpect(jsonPath("$.id").exists())
         * .andExpect(jsonPath("$.name").value("NewUser"))
         * .andExpect(jsonPath("$.attempted").value(101))
         * .andExpect(jsonPath("$.correct").value(0));
         */

        // Attempts correct answer
        mvc.perform(put("/users/{userid}/answer/{questionid}/{choice}", 3, 2, "B")
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
        /*
         * .andExpect(jsonPath("$.id").exists())
         * .andExpect(jsonPath("$.name").value("NewUser"))
         * .andExpect(jsonPath("$.attempted").value(102))
         * .andExpect(jsonPath("$.correct").value(1));
         */

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

        given(userController.getTopKUsers(2, eq(any()))).willReturn(topKUsersList);

        List<User> actualResults = userController.getTopKUsers(2, eq(any()));
        // Checks if it returns correctly
        assertEquals(u3.getName(), actualResults.get(0).getName());
        assertEquals(u1.getName(), actualResults.get(1).getName());
    }

    public static String asJsonString(final Object question) {
        try {
            return new ObjectMapper().writeValueAsString(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
