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
    public void constructorTest() throws Exception {
        // Creates new question
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");

        assertEquals(new Integer(1), q1.getId());
        assertEquals("What is 1+1?", q1.getName());
        assertEquals("1", q1.getA());
        assertEquals("2", q1.getB());
        assertEquals("3", q1.getC());
        assertEquals("4", q1.getD());
        assertEquals("B", q1.getAnswer());
    }

    // Tests to see if the get id method works properly
    @Test
    public void getIdTest() throws Exception {
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");

        assertEquals(new Integer(1), q1.getId());
    }

    // Tests to see if the get name method works properly
    @Test
    public void getNameTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");

        assertEquals("What is 1+1?", q1.getName());
    }

    // Tests to see if the get attempted method works properly
    @Test
    public void getChoiceATest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");

        assertEquals("1", q1.getA());
    }

    // Tests to see if the get attempted method works properly
    @Test
    public void getChoiceBTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");

        assertEquals("2", q1.getB());
    }

    // Tests to see if the get attempted method works properly
    @Test
    public void getChoiceCTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");

        assertEquals("3", q1.getC());
    }

    // Tests to see if the get attempted method works properly
    @Test
    public void getChoiceDTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");

        assertEquals("4", q1.getD());
    }

    // Tests to see if the get attempted method works properly
    @Test
    public void getAnswerTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");

        assertEquals("B", q1.getAnswer());
    }

    // Tests to see if the set id method works properly
    @Test
    public void setIdTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");
        q1.setId(10);

        assertEquals(new Integer(10), q1.getId());
    }

    // Tests to see if the set name method works properly
    @Test
    public void setNameTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");
        q1.setName("Q2");

        assertEquals("Q2", q1.getName());
    }

    // Tests to see if the set attempted method works properly
    @Test
    public void setChoiceATest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");
        q1.setA("5");

        assertEquals("5", q1.getA());
    }

    // Tests to see if the set attempted method works properly
    @Test
    public void setChoiceBTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");
        q1.setB("6");

        assertEquals("6", q1.getB());
    }

    // Tests to see if the set attempted method works properly
    @Test
    public void setChoiceCTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");
        q1.setC("7");

        assertEquals("7", q1.getC());
    }

    // Tests to see if the set attempted method works properly
    @Test
    public void setChoiceDTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");
        q1.setD("8");

        assertEquals("8", q1.getD());
    }

    // Tests to see if the set attempted method works properly
    @Test
    public void setAnswerTest() throws Exception {
        // Creates 3 users which different score percents
        Question q1 = new Question(1, "What is 1+1?", "1", "2", "3", "4", "B");
        q1.setAnswer("A");

        assertEquals("A", q1.getAnswer());
    }
}
