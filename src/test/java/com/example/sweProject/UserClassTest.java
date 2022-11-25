package com.example.sweProject;

import com.example.sweProject.controllers.UserController;
import com.example.sweProject.entities.Question;
import com.example.sweProject.entities.User;
import com.example.sweProject.repositories.QuestionRepository;
import com.example.sweProject.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONException;
import org.json.JSONObject;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserClassTest {
    @Autowired
    private MockMvc mvc;

    public static String asJsonString(final Object question) {
        try {
            return new ObjectMapper().writeValueAsString(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getBearerToken() {
        HttpResponse<String> response = Unirest.post(
                "https://dev-lb0aibabfhuc6e6j.us.auth0.com/oauth/token")
                .header("content-type", "application/json")
                .body("{\"client_id\":\"LsVAxRmvrm8yxktqXOzdDWWn6mlAxd6P\",\"client_secret\":\"DJeBImCv2Mi6Qbe3_m2mYPwAHSkuJO_YoXm_XlnWRg1B0myVdS4BPhO1BeaeCa3I\",\"audience\":\"localhost:8080\",\"grant_type\":\"client_credentials\"}")
                .asString();

        String responseBody = response.getBody();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            String accessToken = jsonResponse.getString("access_token");
            return "Bearer " + accessToken;
        } catch (JSONException err) {
            return null;
        }
    }

    // Tests to see if the first constructor works properly
    @Test
    public void constructorTest1() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1");

        assertEquals(new Integer(1), u1.getId());
        assertEquals("U1", u1.getName());
        assertEquals(0, u1.getAttempted());
        assertEquals(0, u1.getCorrect());
    }

    // Tests to see if the second constructor works properly
    @Test
    public void constructorTest2() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);

        assertEquals(new Integer(1), u1.getId());
        assertEquals("U1", u1.getName());
        assertEquals(10, u1.getAttempted());
        assertEquals(5, u1.getCorrect());
    }

    // Tests to see if the get id method works properly
    @Test
    public void getIdTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);

        assertEquals(new Integer(1), u1.getId());
    }

    // Tests to see if the get name method works properly
    @Test
    public void getNameTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);

        assertEquals("U1", u1.getName());
    }

    // Tests to see if the get attempted method works properly
    @Test
    public void getAttemptedTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);

        assertEquals(10, u1.getAttempted());
    }

    // Tests to see if the get attempted method works properly
    @Test
    public void getCorrectTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);

        assertEquals(5, u1.getCorrect());
    }

    // Tests to see if the set id method works properly
    @Test
    public void setIdTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);
        u1.setId(10);

        assertEquals(new Integer(10), u1.getId());
    }

    // Tests to see if the set name method works properly
    @Test
    public void setNameTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);
        u1.setName("U2");

        assertEquals("U2", u1.getName());
    }

    // Tests to see if the set attempted method works properly
    @Test
    public void setAttemptedTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);
        u1.setAttempted(20);

        assertEquals(20, u1.getAttempted());
    }

    // Tests to see if the set correct method works properly
    @Test
    public void setCorrectTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(1, "U1", 10, 5);
        u1.setCorrect(6);

        assertEquals(6, u1.getCorrect());
    }
}
