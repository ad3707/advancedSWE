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
public class QuestionClassTest {
    @Autowired
    private MockMvc mvc;

    Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");

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

    // Tests to see if the constructor works properly
    @Test
    void constructorTest() throws Exception {
        // Creates new question
        Question q2 = new Question(1, "What is 1+2?", "1", "2", "3", "4", "C");

        assertEquals(new Integer(1), q2.getId());
        assertEquals("What is 1+2?", q2.getName());
        assertEquals("1", q2.getA());
        assertEquals("2", q2.getB());
        assertEquals("3", q2.getC());
        assertEquals("4", q2.getD());
        assertEquals("C", q2.getAnswer());
    }

    // Tests to see if the get id method works properly
    @Test
    void getIdTest() throws Exception {
        assertEquals(new Integer(1), q1.getId());
    }

    // Tests to see if the get name method works properly
    @Test
    void getNameTest() throws Exception {
        assertEquals("What is 1+1?", q1.getName());
    }

    // Tests to see if the get choice A method works properly
    @Test
    void getChoiceATest() throws Exception {
        assertEquals("1", q1.getA());
    }

    // Tests to see if the get choice B method works properly
    @Test
    void getChoiceBTest() throws Exception {
        assertEquals("2", q1.getB());
    }

    // Tests to see if the get choice C method works properly
    @Test
    void getChoiceCTest() throws Exception {
        assertEquals("3", q1.getC());
    }

    // Tests to see if the get choice D method works properly
    @Test
    void getChoiceDTest() throws Exception {
        assertEquals("4", q1.getD());
    }

    // Tests to see if the get answer method works properly
    @Test
    void getAnswerTest() throws Exception {
        assertEquals("B", q1.getAnswer());
    }

    // Tests to see if the set id method works properly
    @Test
    void setIdTest() throws Exception {
        q1.setId(10);

        assertEquals(new Integer(10), q1.getId());
    }

    // Tests to see if the set name method works properly
    @Test
    void setNameTest() throws Exception {
        q1.setName("Q2");

        assertEquals("Q2", q1.getName());
    }

    // Tests to see if the set choice A method works properly
    @Test
    void setChoiceATest() throws Exception {
        q1.setA("5");

        assertEquals("5", q1.getA());
    }

    // Tests to see if the set choice B method works properly
    @Test
    void setChoiceBTest() throws Exception {
        q1.setB("6");

        assertEquals("6", q1.getB());
    }

    // Tests to see if the set choice C method works properly
    @Test
    void setChoiceCTest() throws Exception {
        q1.setC("7");

        assertEquals("7", q1.getC());
    }

    // Tests to see if the set choice D method works properly
    @Test
    void setChoiceDTest() throws Exception {
        q1.setD("8");

        assertEquals("8", q1.getD());
    }

    // Tests to see if the set answer method works properly
    @Test
    void setAnswerTest() throws Exception {
        q1.setAnswer("A");

        assertEquals("A", q1.getAnswer());
    }
}
