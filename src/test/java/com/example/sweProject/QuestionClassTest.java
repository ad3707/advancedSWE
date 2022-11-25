package com.example.sweProject;

import com.example.sweProject.entities.Question;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionClassTest {
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
    public void constructorTest() throws Exception {
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
    public void getIdTest() throws Exception {
        assertEquals(new Integer(1), q1.getId());
    }

    // Tests to see if the get name method works properly
    @Test
    public void getNameTest() throws Exception {
        assertEquals("What is 1+1?", q1.getName());
    }

    // Tests to see if the get choice A method works properly
    @Test
    public void getChoiceATest() throws Exception {
        assertEquals("1", q1.getA());
    }

    // Tests to see if the get choice B method works properly
    @Test
    public void getChoiceBTest() throws Exception {
        assertEquals("2", q1.getB());
    }

    // Tests to see if the get choice C method works properly
    @Test
    public void getChoiceCTest() throws Exception {
        assertEquals("3", q1.getC());
    }

    // Tests to see if the get choice D method works properly
    @Test
    public void getChoiceDTest() throws Exception {
        assertEquals("4", q1.getD());
    }

    // Tests to see if the get answer method works properly
    @Test
    public void getAnswerTest() throws Exception {
        assertEquals("B", q1.getAnswer());
    }

    // Tests to see if the set id method works properly
    @Test
    public void setIdTest() throws Exception {
        q1.setId(10);

        assertEquals(new Integer(10), q1.getId());
    }

    // Tests to see if the set name method works properly
    @Test
    public void setNameTest() throws Exception {
        q1.setName("Q2");

        assertEquals("Q2", q1.getName());
    }

    // Tests to see if the set choice A method works properly
    @Test
    public void setChoiceATest() throws Exception {
        q1.setA("5");

        assertEquals("5", q1.getA());
    }

    // Tests to see if the set choice B method works properly
    @Test
    public void setChoiceBTest() throws Exception {
        q1.setB("6");

        assertEquals("6", q1.getB());
    }

    // Tests to see if the set choice C method works properly
    @Test
    public void setChoiceCTest() throws Exception {
        q1.setC("7");

        assertEquals("7", q1.getC());
    }

    // Tests to see if the set choice D method works properly
    @Test
    public void setChoiceDTest() throws Exception {
        q1.setD("8");

        assertEquals("8", q1.getD());
    }

    // Tests to see if the set answer method works properly
    @Test
    public void setAnswerTest() throws Exception {
        q1.setAnswer("A");

        assertEquals("A", q1.getAnswer());
    }
}
