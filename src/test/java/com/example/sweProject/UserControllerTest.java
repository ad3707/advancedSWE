package com.example.sweProject;

import org.junit.jupiter.api.Test;

import com.example.sweProject.controllers.UserController;
import com.example.sweProject.entities.User;
import com.example.sweProject.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.when;

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
   UserRepository userRepo;

    //Test to see that a client receives all the users
    @Test
    public void getUsers() throws Exception {
        User u = new User(3,"User1",5,2);
        List<User> allUsers = new ArrayList<User>();
        allUsers.add(u);
        when(userRepo.findAll()).thenReturn(allUsers);
        mvc.perform(get("/users")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User1"))
                .andExpect(jsonPath("$[0].attempted", is(5)))
                .andExpect(jsonPath("$[0].correct", is(2)));
    }


    //Tests if client can post a user 
   @Test
   @Transactional
   public void testPostUser() throws Exception {
      User user = new User(2,"User2",6,2);
      when(userRepo.save(any())).thenReturn(user);
      mvc.perform(post("/users")
               .content(asJsonString(user))
               .contentType("application/json")
               .accept("application/json"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.name").value("User2"))
               .andExpect(jsonPath("$.attempted").value(6))
               .andExpect(jsonPath("$.correct").value(2));
      }

   public static String asJsonString(final Object question) {
      try {
          return new ObjectMapper().writeValueAsString(question);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
  }


   //Tests if client can update a user
   @Test
   @Transactional //ensures that the interactions you have with the database are rolled back at the end of each test
   public void testUpdateUsers() throws Exception {
        User originalUser = new User(31, "User31",5,3);
        User newUser= new User(31, null,6,4);
        when(userRepo.findById(any())).thenReturn(Optional.of(originalUser));
    
        mvc.perform(put("/users/{id}", 31)
                .content(asJsonString(newUser))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("User31"))
                .andExpect(jsonPath("$.attempted").value("6"))
                .andExpect(jsonPath("$.correct").value("4"));
    }

    @Test
    @Transactional
    public void testDeleteUser() throws Exception {
        User userToDelete = new User(31, "User2",5,3);
        when(userRepo.findById(any())).thenReturn(Optional.of(userToDelete));
        
        mvc.perform(delete("/users/{id}", 31))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("User2"))
                .andExpect(jsonPath("$.attempted").value(5))
                .andExpect(jsonPath("$.correct").value(3));
    }
}