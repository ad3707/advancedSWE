package com.example.sweProject;

import org.junit.jupiter.api.Test;

import com.example.sweProject.controllers.QuestionController;
import com.example.sweProject.entities.Question;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
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
// import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.aspectj.lang.annotation.Before;
import org.json.simple.JSONObject;    

// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import org.junit.*;
// import static org.mockito.Mockito.*;

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
   private QuestionController questionController;

    //Test to see that a client receives all the questions in the database
    @Test
    public void getQuestions() throws Exception {
        // Question q = new Question();
        // q.setName("What is 1+1?");
        // q.setA("1");
        // q.setB("2");
        // q.setC("3");
        // q.setD("4");
        // q.setAnswer("A");

        // questionController = mock(QuestionController.class);

        List allQuestions = singletonList(new Question(null, "What is 1+1?", "1","2","3","4","A"));

        given(questionController.getAllQuestions()).willReturn(allQuestions);

        mvc.perform(get("/questions")
                //  .contentType("application/json"))
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("What is 1+1?")))
                .andExpect(jsonPath("$[0].a", is("1")));
    }

   //Test for the POST json request. Tests if a user can add a question
   @Test
   public void testPostQuestion() throws Exception {

    Question q = new Question(1, "new question", "1", "2", "3", "4", "A");
    given(questionController.createNewQuestion(q)).willReturn(q);
    given(questionController.getAllQuestions()).willReturn(singletonList(q));

    mvc.perform( MockMvcRequestBuilders
        .post("/questions")
        .content(asJsonString(q))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mvc.perform(get("/questions")
        .accept("application/json"))
        .andExpect(status().isOk())
        //.andExpect(jsonPath("$[0].name".value(("new question")))
        .andExpect(jsonPath("$[0].name").value("1 + 2"))
        .andExpect(jsonPath("$[0].a").value("1"));
        //.andExpect(jsonPath("$[0].a", is("1")));
   }

   
   // act, arrange, assert

   @Test
   public void createQuestionAPI() throws Exception {      
      Question question = new Question(30,"What is 1+1?","1","2","3","4","c");
    //    question.setId(30);
    //    question.setName("What is 1+1?");
    //    question.setA("1");
    //    question.setB("2");
    //    question.setC("3");
    //    question.setD("4");
    //    question.setAnswer("c");

      given(questionController.createNewQuestion(question)).willReturn(question);

      mvc.perform( MockMvcRequestBuilders
               .post("/questions")
               .content(asJsonString(question))
               .contentType("application/json"))
               .andExpect(status().isOk());
               //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
   }

   public static String asJsonString(final Object question) {
      try {
          return new ObjectMapper().writeValueAsString(question);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
  }


  //Test for the PUT API. Tests if client can update a question
//   @Test
//   @Transactional //ensures that the interactions you have with the database are rolled back at the end of each test
//   public void updateQuestionAPI() throws Exception {
//     Question question = new Question(31,"What is 1+2?","1","2","3","4","c");

//     mvc.perform(put("/questions/{id}",31)
//                 .content(asJsonString(question))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name", is("What is 1+2?")))
//                .andExpect(jsonPath("$[0].a").value("1"))
//                .andExpect(jsonPath("$[0].b").value("2"))
//                .andExpect(jsonPath("$[0].c").value("3"))
//                .andExpect(jsonPath("$[0].d").value("4"))
//                .andExpect(jsonPath("$[0].answer").value("c"));
//   }

// //Test for the DELETE API. Tests if a client can delete a quesiton.
// @Test
// public void deleteQuestionAPI() throws Exception {
//    mvc.perform( MockMvcRequestBuilders.delete("/delete/{id}",3))
//                .andExpect(status().isAccepted());
//    }

}
