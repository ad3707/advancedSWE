package com.example.sweProject;

import org.junit.jupiter.api.Test;

import com.example.sweProject.controllers.QuestionController;
import com.example.sweProject.entities.Question;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.hamcrest.core.Is.is;

import java.util.*;
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
}
