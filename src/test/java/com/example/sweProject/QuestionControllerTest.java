package com.example.sweProject;

import org.junit.jupiter.api.Test;

import com.example.sweProject.controllers.QuestionController;
import com.example.sweProject.entities.Question;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

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
       Question q = new Question();
       q.setName("What is 1+1?");
       q.setA("1");
       q.setB("2");
       q.setC("3");
       q.setD("4");
       q.setAnswer("A");

       List allQuestions = singletonList(q);

       given(questionController.getAllQuestions()).willReturn(allQuestions);

       mvc.perform(get("/questions")
               .contentType("application/json"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("name", is(q.getName())))
               .andExpect(jsonPath("A", is(q.getA())));
   }

   //Test for the POST json request. Tests if a user can add a question
   @Test
   public void createQuestionAPI() throws Exception {
      Question question = new Question();
       question.setName("What is 1+1?");
       question.setA("1");
       question.setB("2");
       question.setC("3");
       question.setD("4");
       question.setAnswer("A");

      mvc.perform( MockMvcRequestBuilders
               .post("/questions")
               .content(asJsonString(question))
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.questionID").exists());
   }

   public static String asJsonString(final Object obj) {
      try {
          return new ObjectMapper().writeValueAsString(obj);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
  }


//Test for the PUT API. Tests if client can update a question
  @Test
  @Transactional //ensures that the interactions you have with the database are rolled back at the end of each test.
  public void updateQuestioAPI() throws Exception {

   Question question = new Question();
   question.setName("What is 1+2?");
   question.setA("1");
   question.setB("2");
   question.setC("3");
   question.setD("4");
   question.setAnswer("C");

   mvc.perform( MockMvcRequestBuilders
               .put("/update/{id}",3)
               .content(asJsonString(question))
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("What is 1+2?"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.a").value("1"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.b").value("2"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.c").value("3"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.d").value("4"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.answer").value("C"));
  }



//Test for the DELETE API. Tests if a client can delete a quesiton.
@Test
public void deleteQuestionAPI() throws Exception {
   mvc.perform( MockMvcRequestBuilders.delete("/delete/{id}",3))
               .andExpect(status().isAccepted());
   }
  
}
