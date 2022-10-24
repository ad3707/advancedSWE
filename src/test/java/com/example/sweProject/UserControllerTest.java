package com.example.sweProject;

import org.junit.jupiter.api.Test;

import com.example.sweProject.controllers.UserController;
import com.example.sweProject.entities.User;
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
public class UserControllerTest {
	@Autowired
	private MockMvc mvc;

   @MockBean
   private UserController userController;

    //Test to see that a client receives all the questions in the database
    @Test
    public void getUsers() throws Exception {
        List allUsers = singletonList(new User(null, "SomeUser"));

        given(userController.getAllUsers()).willReturn(allUsers);

        mvc.perform(get("/users")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("SomeUser")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

   //Test for the POST json request. Tests if a user can add a question
   @Test
   public void testPostUser() throws Exception {
    User user = new User(1,"User1");
    given(userController.createNewUser(user)).willReturn(user);
    given(userController.getAllUsers()).willReturn(singletonList(user));

    mvc.perform( MockMvcRequestBuilders
        .post("/users")
        .content(asJsonString(user))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mvc.perform(get("/users")
        .accept("application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("User1")))
        .andExpect(jsonPath("$[0].id", is(1)));
   }

   
   // act, arrange, assert

   @Test
   public void createUserAPI() throws Exception {      
      User user = new User(20,"NewUser");

      given(userController.createNewUser(user)).willReturn(user);

      mvc.perform( MockMvcRequestBuilders
               .post("/users")
               .content(asJsonString(user))
               .contentType("application/json"))
               .andExpect(status().isOk());
               //.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
   }

    //Test for the DELETE API. Tests if a client can delete a quesiton.
    @Test
    public void deleteUserAPI() throws Exception {
        
       User user = new User(20,"NewUser");
       mvc.perform( MockMvcRequestBuilders.delete("/users/{id}",20))
                   .andExpect(status().isAccepted());
       }


   public static String asJsonString(final Object question) {
      try {
          return new ObjectMapper().writeValueAsString(question);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
  }


  //Test for the PUT API. Tests if client can update a question
  @Test
  @Transactional //ensures that the interactions you have with the database are rolled back at the end of each test
  public void updateUserAPI() throws Exception {

    User user = new User(20,"NewUser");

    JSONObject newUser = new JSONObject();    
    newUser.put("name", "User2");
    newUser.put("attempted", 5);   
    newUser.put("correct", 7);   

    mvc.perform(put("/users/{id}",20)
               .content(newUser.toJSONString())
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               //.andExpect(jsonPath("$[0].name", is("User2")))
               .andExpect(jsonPath("$[0].name").value("User2"))
               .andExpect(jsonPath("$[0].attempted").value(5))
               .andExpect(jsonPath("$[0].correct").value(7));
  }

}