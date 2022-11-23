package com.example.sweProject;

import com.example.sweProject.entities.Question;
import com.example.sweProject.repositories.QuestionRepository;
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
public class QuestionControllerTest {
    private final String bearerToken =
            "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6InEyM0NreTdaTjROdEQ0R0Z6TGhhVSJ9.eyJpc3MiOiJodHRwczovL2Rldi1sYjBhaWJhYmZodWM2ZTZqLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJMc1ZBeFJtdnJtOHl4a3RxWE96ZERXV242bWxBeGQ2UEBjbGllbnRzIiwiYXVkIjoibG9jYWxob3N0OjgwODAiLCJpYXQiOjE2NjkxNzk0MjYsImV4cCI6MTY2OTI2NTgyNiwiYXpwIjoiTHNWQXhSbXZybTh5eGt0cVhPemREV1duNm1sQXhkNlAiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.XCOPvcWL24a4iuq42R78OybdzYhW-7HFXL-i22UuwpmT4tNtU42gmFoQFIZ7wEotcn7vPdffLPQdJ3v-c8uuJAArdJMWB2zx8nFl4w__CciB9RuxxASqBmfrcE4e-2mzjQ3fyRnyKIb3pSSmB_c22-YX128B4fFmvlmDNr2Gp7_akxXrpnQSoScd-be9yS5fb1QI9-bKlRTatMTTMum7elWrDOw-MyYHmFshs-pDWud30vHgSDTLZUxyTv3m89gJfA_0HKEiclBwH0u5CygVqnQBmLpxdvXWrYgfcMYV1Q5ibnEe4Gc1a90AuMQ_MaV9yE2qIZOoogdsnQlvQ9Fwrg";
    @MockBean
    QuestionRepository questionRepo;
    @Autowired
    private MockMvc mvc;

    public static String asJsonString(final Object question) {
        try {
            return new ObjectMapper().writeValueAsString(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Test to see that unauthorized get requests fail
    @Test
    public void testUnauthorizedGetQuestionsRequest() throws Exception {
        Question q = new Question(2, "What is 1+1?", "1", "2", "3", "4", "A");
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);

        when(questionRepo.findByClientId(any())).thenReturn(allQuestions);

        mvc.perform(get("/questions")
                        .accept("application/json"))
                .andExpect(status().isUnauthorized());
    }

    // Test to see that a client receives all the questions in the database
    @Test
    public void testGetQuestions() throws Exception {
        Question q = new Question(2, "What is 1+1?", "1", "2", "3", "4", "A");
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);

        when(questionRepo.findByClientId(any())).thenReturn(allQuestions);

        mvc.perform(get("/questions")
                        .header("authorization",
                                bearerToken)
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
                        .header("authorization",
                                bearerToken)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.a").value("1"))
                .andExpect(jsonPath("$.b").value("2"))
                .andExpect(jsonPath("$.c").value("3"))
                .andExpect(jsonPath("$.d").value("4"))
                .andExpect(jsonPath("$.answer").value("C"));
    }

    // Test to see if a user can add an incomplete question
    @Test
    @Transactional
    public void testPostIncompleteQuestion() throws Exception {
        Question q =
                new Question(2, "What is 1 + 0?", "1", null, null, null, "a");

        when(questionRepo.save(any())).thenReturn(q);

        mvc.perform(post("/questions")
                        .content(asJsonString(q))
                        .contentType("application/json")
                        .header("authorization",
                                bearerToken)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.a").value("1"))
                .andExpect(jsonPath("$.b").doesNotExist())
                .andExpect(jsonPath("$.c").doesNotExist())
                .andExpect(jsonPath("$.d").doesNotExist())
                .andExpect(jsonPath("$.answer").value("a"));
    }

    // Tests if client can update a question
    @Test
    @Transactional
    // ensures that the interactions you have with the database are rolled back at
    // the end of each test
    public void testUpdateQuestions() throws Exception {
        Question originalQuestion =
                new Question(31, "What is 1+1?", "1", "2", "3", "4", "b");
        Question newQuestion =
                new Question(31, "What is 1+2?", null, null, null, null, "c");

        when(questionRepo.findBySpecificQuestion(any(), any())).thenReturn(
                Optional.of(originalQuestion));

        mvc.perform(put("/questions/{id}", 31)
                        .content(asJsonString(newQuestion))
                        .contentType("application/json")
                        .header("authorization",
                                bearerToken)
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

    // Tests if client can update a question that does not exist
    @Test
    @Transactional
    // ensures that the interactions you have with the database are rolled back at
    // the end of each test
    public void testUpdateNonexistentQuestions() throws Exception {
        Question newQuestion =
                new Question(31, "What is 1+2?", null, null, null, null, "c");

        mvc.perform(put("/questions/{id}", 31)
                        .content(asJsonString(newQuestion))
                        .contentType("application/json")
                        .header("authorization",
                                bearerToken)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    // Tests if a client can delete a question
    @Test
    @Transactional
    public void testDeleteQuestion() throws Exception {
        Question questionToDelete =
                new Question(31, "What is 1+1?", "1", "2", "3", "4", "b");
        when(questionRepo.findBySpecificQuestion(any(), any())).thenReturn(
                Optional.of(questionToDelete));

        mvc.perform(delete("/questions/{id}", 31)
                        .header("authorization",
                                bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("What is 1+1?"))
                .andExpect(jsonPath("$.a").value("1"))
                .andExpect(jsonPath("$.b").value("2"))
                .andExpect(jsonPath("$.c").value("3"))
                .andExpect(jsonPath("$.d").value("4"))
                .andExpect(jsonPath("$.answer").value("b"));

    }

    // Tests if a client can attempt to delete a question that does not exist
    @Test
    @Transactional
    public void testDeleteNonexistentQuestion() throws Exception {
        mvc.perform(delete("/questions/{id}", 100)
                        .header("authorization",
                                bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());

    }
}
