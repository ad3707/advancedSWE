package com.example.sweProject;

import com.example.sweProject.entities.Question;
import com.example.sweProject.repositories.QuestionRepository;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    QuestionRepository questionRepo;

    // Test to see that a client receives all the questions in the database
    @Test
    public void testGetQuestions() throws Exception {
        Question q = new Question(2, "What is 1+1?", "1", "2", "3", "4", "A");
        List<Question> allQuestions = new ArrayList<Question>();
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);

        when(questionRepo.findByClientId(any())).thenReturn(allQuestions);

        mvc.perform(get("/questions")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("What is 1+1?"))
                .andExpect(jsonPath("$[0].a").value("1"))
                .andExpect(jsonPath("$[0].b").value("2"))
                .andExpect(jsonPath("$[0].c").value("3"))
                .andExpect(jsonPath("$[0].d").value("4"))
                .andExpect(jsonPath("$[0].answer").value("A"));
    }

    // Test to see if a user can add a question
    @Test
    @Transactional
    public void testPostQuestion() throws Exception {
        Question q = new Question(1, "What is 1 + 2?", "1", "2", "3", "4", "C");

        when(questionRepo.save(any())).thenReturn(q);

        mvc.perform(post("/questions")
                .content(asJsonString(q))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.a").value("1"))
                .andExpect(jsonPath("$.b").value("2"))
                .andExpect(jsonPath("$.c").value("3"))
                .andExpect(jsonPath("$.d").value("4"))
                .andExpect(jsonPath("$.answer").value("C"));
    }

    // Tests if client can update a question
    @Test
    @Transactional // ensures that the interactions you have with the database are rolled back at
                   // the end of each test
    public void testUpdateQuestions() throws Exception {
        Question originalQuestion = new Question(31, "What is 1+1?", "1", "2", "3", "4", "b");
        Question newQuestion = new Question(31, "What is 1+2?", null, null, null, null, "c");

        when(questionRepo.findBySpecificQuestion(any(), any())).thenReturn(Optional.of(originalQuestion));

        mvc.perform(put("/questions/{id}", 31)
                .content(asJsonString(newQuestion))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("What is 1+2?"))
                .andExpect(jsonPath("$.a").value("1"))
                .andExpect(jsonPath("$.b").value("2"))
                .andExpect(jsonPath("$.c").value("3"))
                .andExpect(jsonPath("$.d").value("4"))
                .andExpect(jsonPath("$.answer").value("c"));
    }

    // Tests if a client can delete a quesiton
    @Test
    @Transactional
    public void testDeleteQuestion() throws Exception {
        Question questionToDelete = new Question(31, "What is 1+1?", "1", "2", "3", "4", "b");
        when(questionRepo.findBySpecificQuestion(any(), any())).thenReturn(Optional.of(questionToDelete));

        mvc.perform(delete("/questions/{id}", 31))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("What is 1+1?"))
                .andExpect(jsonPath("$.a").value("1"))
                .andExpect(jsonPath("$.b").value("2"))
                .andExpect(jsonPath("$.c").value("3"))
                .andExpect(jsonPath("$.d").value("4"))
                .andExpect(jsonPath("$.answer").value("b"));

    }

    public static String asJsonString(final Object question) {
        try {
            return new ObjectMapper().writeValueAsString(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
