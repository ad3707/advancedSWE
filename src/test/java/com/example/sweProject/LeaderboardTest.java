package com.example.sweProject;

import org.junit.jupiter.api.Test;

import com.example.sweProject.controllers.UserController;
import com.example.sweProject.entities.User;
import com.example.sweProject.repositories.QuestionRepository;
import com.example.sweProject.controllers.QuestionController;
import com.example.sweProject.entities.Question;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.aspectj.lang.annotation.Before;
import org.json.simple.JSONObject;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.hamcrest.core.Is.is;

import java.util.*;

import javax.transaction.Transactional;

import static java.util.Collections.singletonList;

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
    @Transactional // ensures that the increment operations work as expected
    public void updateUserAPI() throws Exception {

        User user = new User(20, "NewUser");
        user.setAttempted(100);
        user.setCorrect(0);

        Question q = new Question(2, "What is 1+1?", "1", "2", "3", "4", "B");

        userRepo.save(user);
        questionRepo.save(q);

        // Attempts incorrect answer
        mvc.perform(post("/users/{id}/answer/{questionid}", 20, 2)
                .content(asJsonString("A"))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("NewUser"))
                .andExpect(jsonPath("$.attempted").value(101))
                .andExpect(jsonPath("$.correct").value(0));

        // Attempts correct answer
        mvc.perform(post("/users/{id}/answer/{questionid}", 20, 2)
                .content(asJsonString("B"))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("NewUser"))
                .andExpect(jsonPath("$.attempted").value(102))
                .andExpect(jsonPath("$.correct").value(1));

    }

    @Test
    public void getTopKUsers() throws Exception {
        User u1 = new User(1, "U1");
        u1.setCorrect(5);
        u1.setAttempted(10);

        User u2 = new User(2, "U2");
        u2.setCorrect(5);
        u2.setAttempted(20);

        User u3 = new User(3, "U3");
        u3.setCorrect(5);
        u3.setAttempted(5);

        userRepo.save(u1);
        userRepo.save(u2);
        userRepo.save(u3);

        List<User> topKUsersList = new ArrayList<>();
        topKUsersList.add(u3);
        topKUsersList.add(u1);

        given(userController.getTopKUsers(2)).willReturn(topKUsersList);

        List<User> actualResults = userController.getTopKUsers(2);
        assertEquals(u3.getName(), actualResults.get(0).getName());
        assertEquals(u1.getName(), actualResults.get(1).getName());
    }

}
