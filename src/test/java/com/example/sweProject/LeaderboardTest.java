// package com.example.sweProject;

// import org.junit.jupiter.api.Test;

// import com.example.sweProject.controllers.UserController;
// import com.example.sweProject.entities.User;
// import com.example.sweProject.controllers.QuestionController;
// import com.example.sweProject.entities.Question;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
// import org.springframework.beans.factory.annotation.Autowired;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import static org.mockito.BDDMockito.given;
// import static org.mockito.BDDMockito.willReturn;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.MediaType;
// import org.aspectj.lang.annotation.Before;
// import org.json.simple.JSONObject;    

// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// import static org.hamcrest.core.Is.is;

// import java.util.*;

// import javax.transaction.Transactional;

// import static java.util.Collections.singletonList;


// @SpringBootTest
// @AutoConfigureMockMvc
// public class LeaderboardTest {
// 	@Autowired
// 	private MockMvc mvc;

//    @MockBean
//    private UserController userController;

//    @MockBean
//    private QuestionController questionController;


//   @Test
//   @Transactional // ensures that the increment operations work as expected
//   public void updateUserAPI() throws Exception {

//     User user = new User(20,"NewUser");

//     JSONObject newUser = new JSONObject();    
//     newUser.put("name", "User2");
//     newUser.put("attempted", 5);   
//     newUser.put("correct", 7);   

//     mvc.perform(put("/users/{id}",20)
//                .content(newUser.toJSONString())
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name", is("User2")))
//                .andExpect(jsonPath("$[0].name").value(5))
//                .andExpect(jsonPath("$[0].attempted").value(5))
//                .andExpect(jsonPath("$[0].correct").value(7));
//   }



//     @Test
//     public void getTopKUsers() throws Exception {
//         User u1 = new User(1,"U1");
//         u1.setCorrect(5); u1.setAttempted(10); 

//         User u2 = new User(2,"U2");
//         u2.setCorrect(5); u2.setAttempted(20); 

//         User u3 = new User(3,"U3");
//         u3.setCorrect(5); u3.setAttempted(5); 


//         List<User> topKUsersList = new ArrayList<>();
//         topKUsersList.add(u3); topKUsersList.add(u1);

//         given(userController.getTopKUsers(2)).willReturn(topKUsersList);

//         List<User> actualResults = userController.getTopKUsers(2);
//         assertEquals(u3.getName(), actualResults.get(0).getName());
//         assertEquals(u1.getName(), actualResults.get(1).getName());
//     }

// }


