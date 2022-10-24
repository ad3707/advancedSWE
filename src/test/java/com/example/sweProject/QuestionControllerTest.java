package com.example.sweProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.sweProject.controllers.QuestionController;
import com.example.sweProject.entities.Question;
import com.example.sweProject.repositories.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JsonContent;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
public class QuestionControllerTest {
	@Autowired
	private MockMvc mvc;

    @MockBean
    QuestionRepository questionRepo;

    //Test to see that a client receives all the questions in the database
    @Test
    public void testGetQuestions() throws Exception {
        Question q  = new Question(2, "What is 1+1?", "1","2","3","4","A");
        List<Question> allQuestions = new ArrayList<Question>();
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);
        allQuestions.add(q);

        when(questionRepo.findAll()).thenReturn(allQuestions);

        mvc.perform(get("/questions")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("What is 1+1?"))
                .andExpect(jsonPath("$[0].a", is("1")))
                .andExpect(jsonPath("$[0].b", is("2")))
                .andExpect(jsonPath("$[0].c", is("3")))
                .andExpect(jsonPath("$[0].d", is("4")))
                .andExpect(jsonPath("$[0].answer", is("A")));
    }

   //Test to see if a user can add a question
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

    //Tests if client can update a question
    @Test
    @Transactional //ensures that the interactions you have with the database are rolled back at the end of each test
    public void testUpdateQuestions() throws Exception {
        Question newQuestion = new Question(31, "What is 1+2?","1","2","3","4","C");
        
        when(questionRepo.findById(any())).thenReturn(Optional.of(newQuestion));
    
        mvc.perform(put("/questions/{id}", 31)
                .content(asJsonString(newQuestion))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("What is 1+2?")))
                .andExpect(jsonPath("$.a").value("1"))
                .andExpect(jsonPath("$.b").value("2"))
                .andExpect(jsonPath("$.c").value("3"))
                .andExpect(jsonPath("$.d").value("4"))
                .andExpect(jsonPath("$.answer").value("c"));
    }

// //Test for the DELETE API. Tests if a client can delete a quesiton.
// @Test
// public void deleteQuestionAPI() throws Exception {
//    mvc.perform( MockMvcRequestBuilders.delete("/delete/{id}",3))
//                .andExpect(status().isAccepted());
//    }

    public static String asJsonString(final Object question) {
        try {
            return new ObjectMapper().writeValueAsString(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
